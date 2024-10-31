package com.tinyengine.it.gateway.ai;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.config.AiChatConfig;
import com.tinyengine.it.model.dto.AiMessages;
import com.tinyengine.it.model.dto.OpenAiBodyDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AiChatClientTest {
    @Mock
    private Map<String, AiChatConfig.AiChatConfigData> config;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;
    @InjectMocks
    private AiChatClient aiChatClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteChatRequest() {
        HashMap<String, String> headers = new HashMap<String, String>() { {
            put("headers", "headers");
        }};
        AiChatConfig.HttpRequestOption option =
                new AiChatConfig.HttpRequestOption("POST", "json", "json", 100);
        AiChatConfig.AiChatConfigData configData =
                new AiChatConfig.AiChatConfigData("httpRequestUrl", option, headers, null);

        String modelName = "model";
        when(config.get(modelName)).thenReturn(configData);

        WebClient.RequestBodyUriSpec bodyUriSpec =
                Mockito.mock(WebClient.RequestBodyUriSpec.class, RETURNS_DEEP_STUBS);
        when(webClient.method(any(HttpMethod.class))).thenReturn(bodyUriSpec);
        WebClient.RequestHeadersSpec<?> requestSpec =
                Mockito.mock(WebClient.RequestHeadersSpec.class, RETURNS_DEEP_STUBS);

        when(bodyUriSpec.uri(anyString())).thenReturn(bodyUriSpec);

        AiMessages aiMessages = new AiMessages();
        OpenAiBodyDto param = new OpenAiBodyDto(modelName, Arrays.asList(aiMessages));
        Map<String, Object> result = aiChatClient.executeChatRequest(param);
        Assertions.assertTrue(result.isEmpty());
    }
}

