package com.tinyengine.it.service.app.impl.v1;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.OpenAIConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AiChatV1ServiceImplTest {
    private AiChatV1ServiceImpl service;
    private OpenAIConfig config;

    @BeforeEach
    void setUp() {
        config = new OpenAIConfig();
        service = new AiChatV1ServiceImpl(config);
    }

    // === 无白名单模式（严格校验）===

    @Test
    void shouldAllowPublicHttpsUrl() {
        assertDoesNotThrow(() ->
            service.validateFinalUrl("https://api.openai.com/v1/chat/completions"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "http://127.0.0.1:8080/v1/chat/completions",
        "http://localhost:11434/v1/chat/completions",
        "https://192.168.1.1/v1/chat/completions",
        "https://10.0.0.1/v1/chat/completions",
        "https://169.254.169.254/latest/meta-data/"
    })
    void shouldRejectInternalAddresses(String url) {
        assertThrows(ServiceException.class, () ->
            service.validateFinalUrl(url));
    }

    @Test
    void shouldRejectHttpForPublicHost() {
        assertThrows(ServiceException.class, () ->
            service.validateFinalUrl("http://api.openai.com/v1/chat/completions"));
    }

    @Test
    void shouldRejectInvalidUrl() {
        assertThrows(ServiceException.class, () ->
            service.validateFinalUrl("not-a-valid-url"));
    }

    // === 白名单模式 ===

    @Test
    void shouldAllowWhitelistedHost() {
        config.setAllowedHosts(List.of("api.deepseek.com", "api.openai.com"));
        assertDoesNotThrow(() ->
            service.validateFinalUrl("https://api.deepseek.com/v1/chat/completions"));
    }

    @Test
    void shouldRejectNonWhitelistedHost() {
        config.setAllowedHosts(List.of("api.deepseek.com"));
        assertThrows(ServiceException.class, () ->
            service.validateFinalUrl("https://api.openai.com/v1/chat/completions"));
    }

    @Test
    void shouldAllowWhitelistedLocalhostWithHttp() {
        config.setAllowedHosts(List.of("localhost", "127.0.0.1"));
        assertDoesNotThrow(() ->
            service.validateFinalUrl("http://localhost:11434/v1/chat/completions"));
    }

    @Test
    void shouldRejectHttpForWhitelistedExternalHost() {
        config.setAllowedHosts(List.of("api.deepseek.com"));
        assertThrows(ServiceException.class, () ->
            service.validateFinalUrl("http://api.deepseek.com/v1/chat/completions"));
    }
}
