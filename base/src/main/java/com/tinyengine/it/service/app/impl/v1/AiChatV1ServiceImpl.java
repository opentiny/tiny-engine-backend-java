/**
 * Copyright (c) 2023 - present TinyEngine Authors. Copyright (c) 2023 - present Huawei Cloud
 * Computing Technologies Co., Ltd.
 *
 * <p>Use of this source code is governed by an MIT-style license.
 *
 * <p>THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */
package com.tinyengine.it.service.app.impl.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.common.utils.SM4Utils;
import com.tinyengine.it.config.OpenAIConfig;
import com.tinyengine.it.model.dto.ChatRequest;
import com.tinyengine.it.service.app.v1.AiChatV1Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The type AiChat v1 service.
 *
 * @since 2025-08-06
 */
@Slf4j
@Service
@SuppressWarnings({
    "PMD.AvoidDuplicateLiterals",
    "PMD.AvoidFinalLocalVariable",
    "PMD.AvoidUsingHardCodedIP",
    "PMD.CommentDefaultAccessModifier",
    "PMD.CyclomaticComplexity",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.DefaultPackage",
    "PMD.ExcessiveImports",
    "PMD.GodClass",
    "PMD.LawOfDemeter",
    "PMD.LocalVariableCouldBeFinal",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.ModifiedCyclomaticComplexity",
    "PMD.NPathComplexity",
    "PMD.OnlyOneReturn",
    "PMD.PrematureDeclaration",
    "PMD.PreserveStackTrace",
    "PMD.StdCyclomaticComplexity",
    "PMD.TooManyMethods",
    "PMD.UselessParentheses",
})
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The configuration is a Spring-managed collaborator and is not exposed.")
public class AiChatV1ServiceImpl implements AiChatV1Service {
    private static final int HTTP_OK = 200;
    private static final int STREAM_BUF_SIZE = 8192;
    private static final int BYTE_MASK = 0xFF;
    private static final int IPV4_CG_FIRST = 100;
    private static final int IPV4_CG_MIN = 64;
    private static final int IPV4_CG_MAX = 127;
    private static final int IPV4_192 = 192;
    private static final int IPV4_198 = 198;
    private static final int IPV4_BENCH_A = 18;
    private static final int IPV4_BENCH_B = 19;
    private static final int IPV4_DOC_51 = 51;
    private static final int IPV4_DOC_100 = 100;
    private static final int IPV4_DOC_203 = 203;
    private static final int IPV4_DOC_113 = 113;
    private static final int IPV4_RESERVED = 240;
    private static final int IPV6_UNIQUE = 0xFE;
    private static final int IPV6_UNIQUE_MASK = 0xFC;
    private static final int IPV6_DOC_FIRST = 0x20;
    private static final int IPV6_DOC_SECOND = 0x01;
    private static final int IPV6_DOC_THIRD = 0x0D;
    private static final int IPV6_DOC_FOURTH = 0xB8;
    private static final int IPV6_MULTICAST = 0xFF;
    private static final int IPV6_FOURTH_IDX = 3;
    private static final String EKEY_PREFIX = "EKEY_";
    private static final Set<String> LOOPBACK_HOSTS =
            Set.of("localhost", "127.0.0.1", "::1", "[::1]");

    private final OpenAIConfig config;
    private final HttpClient httpClient;

    public AiChatV1ServiceImpl(OpenAIConfig config) {
        this.config = config;
        this.httpClient =
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                        .followRedirects(HttpClient.Redirect.NEVER)
                        .build();
    }

    /**
     * chatCompletion.
     *
     * @param request the request
     * @return Object the Object
     */
    @Override
    @SystemServiceLog(description = "chatCompletion")
    public Object chatCompletion(ChatRequest request) throws Exception {
        String requestBody = buildRequestBody(request);
        String encryptApiKey =
                request.getApiKey() != null ? request.getApiKey() : config.getApiKey();
        String apiKey = getApiKey(encryptApiKey);
        String normalizedUrl = normalizeApiUrl(request.getBaseUrl());

        // 对最终请求 URL 做安全校验（在 normalize 之后，确保校验的是真正发出的地址）
        URI requestUri = validateFinalUrl(normalizedUrl);

        HttpRequest.Builder requestBuilder =
                HttpRequest.newBuilder()
                        .uri(requestUri)
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + apiKey)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody));
        if (request.isStream()) {
            requestBuilder.header("Accept", "text/event-stream");
            return processStreamResponse(requestBuilder);
        } else {
            return processStandardResponse(requestBuilder);
        }
    }

    /**
     * get token.
     *
     * @param apiKey the apiKey
     * @return token the token
     */
    @Override
    public String getToken(String apiKey) throws GeneralSecurityException {
        String sm4Key = System.getenv("SM4KEY");
        String encrypt = SM4Utils.encrypt(apiKey, sm4Key);
        return EKEY_PREFIX + encrypt;
    }

    /**
     * 规范化API URL，兼容不同厂商.
     *
     * @return normalized API URL
     */
    private String normalizeApiUrl(final String requestedUrl) {
        final String configuredUrl =
                requestedUrl == null || requestedUrl.isBlank()
                        ? config.getBaseUrl()
                        : requestedUrl;
        final String normalizedUrl = configuredUrl.trim();

        if (normalizedUrl.contains("/chat/completions")
                || normalizedUrl.contains("/v1/chat/completions")) {
            return ensureUrlProtocol(normalizedUrl);
        }

        if (normalizedUrl.contains("v1")) {
            return ensureUrlProtocol(normalizedUrl) + "/chat/completions";
        }
        if (normalizedUrl.endsWith("#")) {
            return ensureUrlProtocol(normalizedUrl);
        } else {
            return ensureUrlProtocol(normalizedUrl) + "/v1/chat/completions";
        }
    }

    /**
     * 确保URL有正确的协议前缀.
     *
     * @return URL with protocol
     */
    private String ensureUrlProtocol(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        // 默认使用https
        return "https://" + url;
    }

    private String buildRequestBody(ChatRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(
                "model",
                request.getModel() != null ? request.getModel() : config.getDefaultModel());
        body.put("messages", request.getMessages());
        body.put("stream", request.isStream());
        body.put("tools", request.getTools());
        if (request.getMaxTokens() != null) {
            body.put("max_tokens", request.getMaxTokens());
        }
        body.put("temperature", request.getTemperature());
        if (request.getTemperature() != null) {
            body.put("temperature", request.getTemperature());
        }
        if (request.getSearchOptions() != null) {
            body.put("stream_options", request.getSearchOptions());
        }
        if (request.getPresencePenalty() != null) {
            body.put("presence_penalty", request.getPresencePenalty());
        }
        if (request.getResponseFormat() != null) {
            body.put("response_format", request.getResponseFormat());
        }
        if (request.getMaxInputTokens() != null) {
            body.put("max_input_tokens", request.getMaxInputTokens());
        }
        if (request.getMaxInputTokens() != null) {
            body.put("vl_high_resolution_images", request.getVlHighResolutionImages());
        }
        if (request.getEnableThinking() != null) {
            body.put("enable_thinking", request.getEnableThinking());
        }
        if (request.getToolChoice() != null) {
            body.put("tool_choice", request.getToolChoice());
        }
        if (request.getStop() != null) {
            body.put("stop", request.getStop());
        }
        if (request.getParallelToolCalls() != null) {
            body.put("parallel_tool_calls", request.getParallelToolCalls());
        }
        if (request.getEnableSearch() != null) {
            body.put("enable_search", request.getEnableSearch());
        }
        if (request.getFrequencyPenalty() != null) {
            body.put("frequency_penalty", request.getFrequencyPenalty());
        }

        return JsonUtils.encode(body);
    }

    private JsonNode processStandardResponse(HttpRequest.Builder requestBuilder) {
        try {
            final HttpResponse<String> response =
                    httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            final String code = String.valueOf(response.statusCode());
            if (response.statusCode() != HTTP_OK) {
                final String errorBody = response.body();

                // 尝试解析错误JSON
                final JsonNode errorNode = JsonUtils.MAPPER.readTree(errorBody);
                final String message = errorNode.get("error").get("message").asText();
                throw new ServiceException(code, message);
            }
            return JsonUtils.MAPPER.readTree(response.body());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ServiceException("500", "AI request interrupted", exception);
        } catch (IOException exception) {
            throw new ServiceException("500", "AI request failed", exception);
        }
    }

    private StreamingResponseBody processStreamResponse(HttpRequest.Builder requestBuilder) {
        return outputStream -> {
            final HttpResponse<InputStream> response;
            try {
                response =
                        httpClient.send(
                                requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new ServiceException("500", "AI request interrupted", exception);
            } catch (IOException exception) {
                throw new ServiceException("500", "AI request failed", exception);
            }

            if (log.isInfoEnabled()) {
                log.info("Received AI API response, status code {}", response.statusCode());
            }

            if (response.statusCode() != HTTP_OK) {
                String errorBody =
                        new String(response.body().readAllBytes(), StandardCharsets.UTF_8);

                if (log.isInfoEnabled()) {
                    log.info("errorBody: {}", errorBody);
                }

                JsonNode errorNode = JsonUtils.MAPPER.readTree(errorBody);
                throw new ServiceException(
                        String.valueOf(response.statusCode()),
                        errorNode.get("error").get("message").asText());
            }

            // 正常流处理逻辑
            try (InputStream inputStream = response.body()) {
                byte[] buffer = new byte[STREAM_BUF_SIZE];
                int bytesRead = inputStream.read(buffer);
                while (bytesRead != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                    bytesRead = inputStream.read(buffer);
                }
            }
        };
    }

    URI validateFinalUrl(String finalUrl) {
        URI uri;
        try {
            uri = new URI(finalUrl);
        } catch (URISyntaxException e) {
            throw new ServiceException("400", "Invalid baseUrl format");
        }

        String host = uri.getHost();
        if (host == null || host.isEmpty()) {
            throw new ServiceException("400", "Invalid baseUrl: missing host");
        }
        final String fragment = uri.getRawFragment();
        if (uri.getUserInfo() != null || (fragment != null && !fragment.isEmpty())) {
            throw new ServiceException(
                    "400", "Invalid baseUrl: user info and fragments are not allowed");
        }

        boolean isLoopback = LOOPBACK_HOSTS.contains(host.toLowerCase(Locale.ROOT));

        List<String> allowedHosts = config.getAllowedHosts();

        if (allowedHosts == null || allowedHosts.isEmpty()) {
            if (!config.isAllowAnyHost()) {
                throw new ServiceException("500", "No AI allowed hosts configured");
            }

            enforceHttpsAndIpCheck(uri, host);
            return uri;
        }

        boolean matched = allowedHosts.stream().anyMatch(allowed -> allowed.equalsIgnoreCase(host));
        if (!matched) {
            throw new ServiceException(
                    "400", "Host not allowed: " + host + ". Allowed hosts: " + allowedHosts);
        }

        if (isLoopback) {
            return uri;
        }

        enforceHttpsAndIpCheck(uri, host);
        return uri;
    }

    void enforceHttpsAndIpCheck(URI uri, String host) {
        String scheme = uri.getScheme();
        if (scheme == null || !"https".equalsIgnoreCase(scheme)) {
            throw new ServiceException("400", "Only HTTPS protocol is allowed for custom baseUrl");
        }

        try {
            InetAddress[] addresses = resolveHostAddresses(host);
            boolean hasBlockedAddress = Arrays.stream(addresses).anyMatch(this::isBlockedAddress);
            if (hasBlockedAddress) {
                throw new ServiceException("400", "Internal network addresses are not allowed");
            }
        } catch (UnknownHostException e) {
            throw new ServiceException("400", "Unable to resolve host: " + host);
        }
    }

    InetAddress[] resolveHostAddresses(String host) throws UnknownHostException {
        return InetAddress.getAllByName(host);
    }

    boolean isBlockedAddress(InetAddress address) {
        if (address.isLoopbackAddress()
                || address.isSiteLocalAddress()
                || address.isLinkLocalAddress()
                || address.isAnyLocalAddress()
                || address.isMulticastAddress()) {
            return true;
        }

        if (address instanceof Inet4Address) {
            return isBlockedIpv4((Inet4Address) address);
        }
        if (address instanceof Inet6Address) {
            return isBlockedIpv6((Inet6Address) address);
        }
        return false;
    }

    private boolean isBlockedIpv4(Inet4Address address) {
        byte[] octets = address.getAddress();
        int first = octets[0] & BYTE_MASK;
        int second = octets[1] & BYTE_MASK;
        int third = octets[2] & BYTE_MASK;

        boolean currentNetwork = first == 0;
        boolean sharedSpace =
                first == IPV4_CG_FIRST && second >= IPV4_CG_MIN && second <= IPV4_CG_MAX;
        boolean protocolAssign = first == IPV4_192 && second == 0 && third == 0;
        boolean documentationA = first == IPV4_192 && second == 0 && third == 2;
        boolean benchmarking =
                first == IPV4_198 && (second == IPV4_BENCH_A || second == IPV4_BENCH_B);
        boolean documentationB =
                first == IPV4_198 && second == IPV4_DOC_51 && third == IPV4_DOC_100;
        boolean documentationC = first == IPV4_DOC_203 && second == 0 && third == IPV4_DOC_113;
        return currentNetwork
                || sharedSpace
                || protocolAssign
                || documentationA
                || benchmarking
                || documentationB
                || documentationC
                || first >= IPV4_RESERVED;
    }

    private boolean isBlockedIpv6(Inet6Address address) {
        byte[] octets = address.getAddress();
        int first = octets[0] & BYTE_MASK;
        int second = octets[1] & BYTE_MASK;

        boolean uniqueLocal = (first & IPV6_UNIQUE) == IPV6_UNIQUE_MASK;
        boolean documentation = false;
        if (first == IPV6_DOC_FIRST && second == IPV6_DOC_SECOND) {
            int third = octets[2] & BYTE_MASK;
            int fourth = octets[IPV6_FOURTH_IDX] & BYTE_MASK;
            documentation = third == IPV6_DOC_THIRD && fourth == IPV6_DOC_FOURTH;
        }
        return uniqueLocal || documentation || first == IPV6_MULTICAST;
    }

    private String getApiKey(String encryptApiKey) throws GeneralSecurityException {
        String sm4Key = System.getenv("SM4KEY");

        if (encryptApiKey.startsWith(EKEY_PREFIX)) {
            String encodedApiKey = encryptApiKey.substring(EKEY_PREFIX.length());
            return SM4Utils.decrypt(encodedApiKey, sm4Key);
        }
        return encryptApiKey;
    }
}
