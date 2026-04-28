package com.tinyengine.it.mcp.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.ChannelOption;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 支持 Streamable HTTP 的 MCP 客户端
 * 统一端点处理所有请求，支持会话管理和动态流升级
 */

public class StreamableMcpClient {
	private static final Logger log = LoggerFactory.getLogger(StreamableMcpClient.class);

	@Getter
	private final String serverUrl;
	private final String protocolVersion;
	private final Duration requestTimeout;
	private final ObjectMapper objectMapper;
	private final WebClient webClient;

	private final AtomicReference<String> sessionIdRef = new AtomicReference<>();
	private final AtomicInteger requestIdGen = new AtomicInteger(1);
	private final ConcurrentHashMap<Integer, Sinks.One<JsonNode>> pendingRequests = new ConcurrentHashMap<>();
	private volatile Flux<ServerSentEvent<String>> sseFlux;

	/**
	 * 构造 Streamable MCP 客户端
	 *
	 * @param serverUrl                MCP 服务器端点（如 https://example.com/mcp）
	 * @param protocolVersion          协议版本（如 "2025-06-18"）
	 * @param connectionTimeoutSeconds 连接超时（秒）
	 * @param requestTimeoutSeconds    请求超时（秒）
	 */
	public StreamableMcpClient(String serverUrl, String protocolVersion,
	                           int connectionTimeoutSeconds, int requestTimeoutSeconds) {
		this.serverUrl = serverUrl;
		this.protocolVersion = protocolVersion;
		this.requestTimeout = Duration.ofSeconds(requestTimeoutSeconds);
		this.objectMapper = new ObjectMapper();

		// 1. 创建 Netty HttpClient，配置自动重定向和超时
		HttpClient httpClient = HttpClient.create()
			.followRedirect(true)  // 自动处理 307 重定向
			.responseTimeout(Duration.ofSeconds(requestTimeoutSeconds))
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutSeconds * 1000); // 连接超时（毫秒）

		// 2. 使用 ReactorClientHttpConnector 包装 HttpClient
		ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
		this.webClient = WebClient.builder()
			.baseUrl(serverUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader("MCP-Protocol-Version", protocolVersion)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ", " + MediaType.TEXT_EVENT_STREAM_VALUE)
			.clientConnector(connector)
			.build();
	}

	// ========== 公共 API ==========

	/**
	 * 初始化会话（发送 initialize 请求）
	 */
	public Mono<Void> initSession() {
		// 构建 initialize 参数
		ObjectNode params = objectMapper.createObjectNode();
		params.put("protocolVersion", this.protocolVersion); // 例如 "2025-06-18"

		// 客户端信息
		ObjectNode clientInfo = objectMapper.createObjectNode();
		clientInfo.put("name", "StreamableMcpClient");
		clientInfo.put("version", "1.0.0");
		params.set("clientInfo", clientInfo);

		// 客户端能力
		ObjectNode capabilities = objectMapper.createObjectNode();
		ObjectNode toolsCap = objectMapper.createObjectNode();
		toolsCap.put("listChanged", true);
		capabilities.set("tools", toolsCap);
		params.set("capabilities", capabilities);
		return sendRequest("initialize", params).then();
	}

	/**
	 * 发送 JSON-RPC 请求，自动管理会话 ID 和流式响应
	 *
	 * @param method 方法名（如 "tools/list"）
	 * @param params 参数（可为 null）
	 * @return 响应结果的 Mono
	 */
	public Mono<JsonNode> sendRequest(String method, @Nullable Object params) {
		int requestId = requestIdGen.getAndIncrement();
		ObjectNode requestBody = createRequestBody(method, params, requestId);
		log.info("发送请求 [id={}, method={}]", requestId, method);
		log.info("请求体: {}", requestBody);

		// 创建 sink 用于接收最终响应
		Sinks.One<JsonNode> sink = Sinks.one();
		pendingRequests.put(requestId, sink);

		return doSendRequest(requestBody, requestId)
			.timeout(requestTimeout)
			.onErrorResume(e -> {
				pendingRequests.remove(requestId);
				return Mono.error(e);
			})
			.then(sink.asMono());
	}

	public Mono<JsonNode> sendGetRequest(String method, @Nullable Object params) {
		int requestId = requestIdGen.getAndIncrement();
		ObjectNode requestBody = createRequestBody(method, params, requestId);
		log.info("发送请求 [id={}, method={}]", requestId, method);
		log.info("请求体: {}", requestBody);

		// 创建 sink 用于接收最终响应
		Sinks.One<JsonNode> sink = Sinks.one();
		pendingRequests.put(requestId, sink);

		return doGetRequest(requestBody, requestId)
			.timeout(requestTimeout)
			.onErrorResume(e -> {
				pendingRequests.remove(requestId);
				return Mono.error(e);
			})
			.then(sink.asMono());
	}

	/**
	 * 主动建立 SSE 流，用于接收服务器后续推送（如进度通知）
	 */
	public Flux<ServerSentEvent<String>> connectStream() {
		if (sseFlux == null) {
			synchronized (this) {
				if (sseFlux == null) {
					sseFlux = createStream();
				}
			}
		}
		return sseFlux;
	}

	/**
	 * 关闭当前会话，释放服务器资源
	 */
	public Mono<Void> closeSession() {
		String sessionId = sessionIdRef.get();
		if (sessionId == null) {
			return Mono.empty();
		}
		return webClient.delete()
			.headers(headers -> headers.set("Mcp-Session-Id", sessionId))
			.retrieve()
			.toBodilessEntity()
			.doOnSuccess(resp -> {
				log.info("会话已关闭: {}", sessionId);
				sessionIdRef.set(null);
			})
			.then();
	}

	// ========== 便捷工具方法 ==========

	public Mono<JsonNode> listTools() {
		return sendRequest("tools/list", null);

	}
	public JsonNode listTools(int requestTimeoutSeconds) {
		return webClient.get()
			.uri("tools/list")
			.accept(MediaType.APPLICATION_JSON,MediaType.TEXT_EVENT_STREAM)
			.headers(headers -> {
				String sessionId = sessionIdRef.get();
				if (sessionId != null) {
					headers.set("Mcp-Session-Id", sessionId);
				}
			})
			.exchangeToMono(response -> {
				updateSessionId(response.headers().asHttpHeaders());

				if (response.statusCode().is2xxSuccessful()) {
					return response.bodyToMono(JsonNode.class);
				} else if (response.statusCode() == HttpStatus.CONFLICT) {
					// 409 表示服务器认为已存在活跃 SSE 流，可能是在同一个会话中重复请求
					// 等待短暂时间后重试
					return Mono.delay(Duration.ofMillis(500))
						.then(Mono.error(new RuntimeException("Conflict, retry later")));
				} else {
					return response.bodyToMono(String.class)
						.flatMap(errorBody -> Mono.error(new RuntimeException(
							"获取工具列表失败，HTTP " + response.statusCode() + ": " + errorBody)));
				}
			})
			.retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
				.maxBackoff(Duration.ofSeconds(3))
				.filter(throwable -> throwable.getMessage().contains("Conflict"))
				.doBeforeRetry(rs -> log.warn("重试获取工具列表，尝试次数: {}", rs.totalRetries() + 1)))
			.timeout(Duration.ofSeconds(requestTimeoutSeconds))
			.doOnSuccess(json -> log.debug("成功获取工具列表，共 {} 个工具",
				json != null && json.isArray() ? json.size() : 0))
			.doOnError(e -> log.error("获取工具列表最终失败: {}", e.getMessage()))
			.block();
	}
	public JsonNode fetchToolsList(int requestTimeoutSeconds) {
		try {
			return webClient.get()
				.uri("tools/list")
				.accept(MediaType.APPLICATION_JSON)
				.headers(headers -> {
					String sid = sessionIdRef.get();
					if (sid != null) {
						headers.set("Mcp-Session-Id", sid);
					}
				})
				.retrieve()
				.bodyToMono(JsonNode.class)
				.block(Duration.ofSeconds(requestTimeoutSeconds));
		} catch (Exception e) {
			log.error("获取工具列表失败", e);
			throw new RuntimeException("无法获取工具列表: " + e.getMessage(), e);
		}
	}
	public Mono<JsonNode> callTool(String toolName, JsonNode arguments) {
		ObjectNode params = objectMapper.createObjectNode();
		params.put("name", toolName);
		params.set("arguments", arguments);
		return sendRequest("tools/call", params);
	}

	// ========== 内部实现 ==========

	/**
	 * 实际发送 HTTP 请求
	 */
	private Mono<Void> doSendRequest(ObjectNode requestBody, int requestId) {
		return webClient.post()
			.accept(MediaType.APPLICATION_JSON,MediaType.TEXT_EVENT_STREAM)
			.body(BodyInserters.fromValue(requestBody))
			.headers(headers -> {
				String sessionId = sessionIdRef.get();
				if (sessionId != null) {
					headers.set("Mcp-Session-Id", sessionId);
				}
			})
			.exchangeToMono(response -> {
				// 更新会话 ID
				updateSessionId(response.headers().asHttpHeaders());

				if (response.statusCode() == HttpStatus.ACCEPTED) {
					// 202 Accepted：升级为流式响应
					return handleStreamingResponse(response, requestId);
				} else if (response.statusCode().is2xxSuccessful()) {
					// 普通 JSON 响应
					return handleNormalResponse(response, requestId);
				} else {
					return handleErrorResponse(response);
				}
			})
			.doOnError(e -> {
				log.error("请求失败 [id={}]", requestId, e);
				Sinks.One<JsonNode> sink = pendingRequests.remove(requestId);
				if (sink != null) {
					sink.tryEmitError(e);
				}
			});
	}

	private Mono<Void> doGetRequest(ObjectNode requestBody,int requestId) {
		return webClient.get()
			.accept(MediaType.APPLICATION_JSON,MediaType.TEXT_EVENT_STREAM)
			.headers(headers -> {
				String sessionId = sessionIdRef.get();
				if (sessionId != null) {
					headers.set("Mcp-Session-Id", sessionId);
				}
			})
			.exchangeToMono(response -> {
				// 更新会话 ID
				updateSessionId(response.headers().asHttpHeaders());

				if (response.statusCode() == HttpStatus.ACCEPTED) {
					// 202 Accepted：升级为流式响应
					return handleStreamingResponse(response, requestId);
				} else if (response.statusCode().is2xxSuccessful()) {
					// 普通 JSON 响应
					return handleNormalResponse(response, requestId);
				} else {
					return handleErrorResponse(response);
				}
			})
			.doOnError(e -> {
				log.error("请求失败 [id={}]", requestId, e);
				Sinks.One<JsonNode> sink = pendingRequests.remove(requestId);
				if (sink != null) {
					sink.tryEmitError(e);
				}
			});
	}

	/**
	 * 处理普通 JSON 响应（ClientResponse 版本）
	 */
	private Mono<Void> handleNormalResponse(ClientResponse response, int requestId) {
		return response.bodyToMono(JsonNode.class)
			.doOnNext(json -> {
				log.debug("收到普通响应 [id={}]: {}", requestId, json);
				Sinks.One<JsonNode> sink = pendingRequests.remove(requestId);
				if (sink != null) {
					if (json.has("error")) {
						JsonNode error = json.get("error");
						String msg = error.path("message").asText();
						String data = error.path("data").isMissingNode() ? "" : error.path("data").toString();
						sink.tryEmitError(new RuntimeException("JSON-RPC error: " + msg + " - " + data));
					} else {
						sink.tryEmitValue(json);
					}
				}
			})
			.then();
	}

	/**
	 * 处理流式响应（202 Accepted）
	 */

	private Mono<Void> handleStreamingResponse(ClientResponse response, int requestId) {
		return response.bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
			})
			.doOnNext(this::handleServerSentEvent)
			.doOnComplete(() -> log.debug("SSE流结束 [id={}]", requestId))
			.then();
	}

	/**
	 * 处理错误响应
	 */
	private Mono<Void> handleErrorResponse(ClientResponse response) {
		return response.bodyToMono(String.class)
			.flatMap(errorBody -> {
				log.error("HTTP错误 [{}]: {}", response.statusCode(), errorBody);
				return Mono.error(new RuntimeException(
					"HTTP " + response.statusCode() + ": " + errorBody));
			});
	}

	/**
	 * 处理服务器推送的 SSE 事件
	 */
	private void handleServerSentEvent(ServerSentEvent<String> sse) {
		if (sse.data() == null) return;
		try {
			JsonNode event = objectMapper.readTree(sse.data());
			if (event.has("id")) {
				int respId = event.get("id").asInt();
				Sinks.One<JsonNode> sink = pendingRequests.remove(respId);
				if (sink != null) {
					if (event.has("error")) {
						sink.tryEmitError(new RuntimeException(event.get("error").toString()));
					} else {
						sink.tryEmitValue(event);
					}
				}
			} else if (event.has("method")) {
				// 服务器发起的通知，可根据需要处理
				log.info("收到服务器通知: {}", event);
				// TODO: 可回复 acknowledgement
			}
		} catch (Exception e) {
			log.error("解析 SSE 事件失败", e);
		}
	}

	/**
	 * 创建 JSON-RPC 请求体
	 */
	private ObjectNode createRequestBody(String method, @Nullable Object params, int id) {
		ObjectNode node = objectMapper.createObjectNode();
		node.put("jsonrpc", "2.0");
		node.put("method", method);
		node.put("id", id);
		if (params == null) {
			node.set("params", objectMapper.createObjectNode());
		} else {
			node.set("params", objectMapper.valueToTree(params));
		}
		return node;
	}

	/**
	 * 从响应头更新会话 ID
	 */
	private void updateSessionId(HttpHeaders headers) {
		String newId = headers.getFirst("Mcp-Session-Id");
		log.info("响应头: {}", headers);
		if (newId != null) {
			String oldId = sessionIdRef.getAndSet(newId);
			if (oldId == null) {
				log.info("会话ID已设置: {}", newId);
			} else if (!oldId.equals(newId)) {
				log.warn("会话ID变更: {} -> {}", oldId, newId);
			}
		}
	}
	public String getSessionId() {
		return sessionIdRef.get();
	}
	/**
	 * 创建并配置主动 SSE 流
	 */
	private Flux<ServerSentEvent<String>> createStream() {
		return webClient.get()
			.headers(headers -> {
				String sessionId = sessionIdRef.get();
				if (sessionId != null) {
					headers.set("Mcp-Session-Id", sessionId);
				}
			})
			.retrieve()
			.bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
			})
			.doOnNext(this::handleServerSentEvent)
			.doOnError(e -> log.error("SSE流异常", e))
			.share();
	}



}