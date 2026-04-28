package com.tinyengine.it.mcp.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.config.OpenAIConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Streamable HTTP 客户端管理器
 * 负责创建、缓存和销毁 StreamableMcpClient 实例，提供统一的工具调用入口
 */
@Component
public class StreamableHttpClientManager {

	private static final Logger log = LoggerFactory.getLogger(StreamableHttpClientManager.class);

	private final OpenAIConfig config;
	private final ObjectMapper objectMapper;
	private final ConcurrentHashMap<String, StreamableMcpClient> clientCache = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, RemoteToolDefinition> toolCache = new ConcurrentHashMap<>();

	public StreamableHttpClientManager(OpenAIConfig config, ObjectMapper objectMapper) {
		this.config = config;
		this.objectMapper = objectMapper;
	}
	public void init() {
		System.out.println("初始化 StreamableHttpClientManager，配置: " + config);
		Set<String> uniqueUrls = new HashSet<>(config.getStreamableTools().values());
		for (String url : uniqueUrls) {
			try {
				StreamableMcpClient client = getOrCreateClient(url);
				client.initSession().block(Duration.ofSeconds(config.getStreamableHttp().getConnectionTimeoutSeconds()));
				log.info("已获取会话 ID: {}", client.getSessionId());
				JsonNode toolsResponse = client.listTools().block(Duration.ofSeconds(config.getStreamableHttp().getRequestTimeoutSeconds()));
				log.info("从 {} 获取工具列表成功，响应: {}", url, toolsResponse);
				if (toolsResponse != null && toolsResponse.has("tools") && toolsResponse.get("tools").isArray()) {
					for (JsonNode toolNode : toolsResponse.get("tools")) {
						RemoteToolDefinition def = new RemoteToolDefinition();
						def.setName(toolNode.get("name").asText());
						if (toolNode.has("description")) {
							def.setDescription(toolNode.get("description").asText());
						}
						def.setParameters(toolNode.get("inputSchema")); // 注意字段名可能为 inputSchema
						def.setServerUrl(url);
						toolCache.put(def.getName(), def);
						log.info("缓存远程工具: {} (来自 {})", def.getName(), url);
					}
				} else {
					log.warn("从 {} 获取工具列表失败，响应格式异常", url);
				}
			} catch (Exception e) {
				log.error("初始化服务 {} 失败: {}", url, e.getMessage());
			}
		}
		log.info("共缓存 {} 个远程工具定义", toolCache.size());
	}

	private StreamableMcpClient getOrCreateClient(String serverUrl) {
		return clientCache.computeIfAbsent(serverUrl, url -> new StreamableMcpClient(
			url,
			config.getStreamableHttp().getProtocolVersion(),
			config.getStreamableHttp().getConnectionTimeoutSeconds(),
			config.getStreamableHttp().getRequestTimeoutSeconds()
		));
	}

	public RemoteToolDefinition getToolDefinition(String toolName) {
		return toolCache.get(toolName);
	}

	public JsonNode callTool(String toolName, JsonNode arguments) {
		RemoteToolDefinition def = toolCache.get(toolName);
		if (def == null) {
			throw new IllegalArgumentException("未找到远程工具定义: " + toolName);
		}
		StreamableMcpClient client = getOrCreateClient(def.getServerUrl());
		try {
			return client.callTool(toolName, arguments)
				.block(Duration.ofSeconds(config.getStreamableHttp().getRequestTimeoutSeconds()));
		} catch (Exception e) {
			log.error("调用远程工具失败: {}", toolName, e);
			throw new RuntimeException("远程工具调用失败: " + e.getMessage(), e);
		}
	}
	public String getSessionId(String serverUrl) {
		StreamableMcpClient client = clientCache.get(serverUrl);
		return client != null ? client.getSessionId() : null;
	}
	@PreDestroy
	public void shutdown() {
		clientCache.values().forEach(client -> {
			try {
				client.closeSession().block(Duration.ofSeconds(5));
			} catch (Exception e) {
				log.warn("关闭客户端失败: {}", client.getServerUrl(), e);
			}
		});
		clientCache.clear();
		toolCache.clear();
	}


}
