package com.tinyengine.it.rag.config;

import okhttp3.OkHttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.DataflowAnomalyAnalysis"
})
/** Checks ChromaDB availability before initializing an embedding store. */
@Component
public final class ChromaConnectionProbe {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChromaConnectionProbe.class);
    private static final int HEALTH_TIMEOUT = 5;
    private final Duration timeout;

    public ChromaConnectionProbe() {
        timeout = Duration.ofSeconds(HEALTH_TIMEOUT);
    }

    public boolean isAvailable(final String baseUrl) {
        boolean available = false;
        try {
            final okhttp3.Request request = createRequest(baseUrl);
            final OkHttpClient client = createClient();
            final okhttp3.Call call = createCall(client, request);
            available = execute(call);
        } catch (IOException exception) {
            logWarn("ChromaDB connection test failed: {}", exception.getMessage());
        }
        return available;
    }

    private okhttp3.Request createRequest(final String baseUrl) {
        final okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(baseUrl + "/api/v1/heartbeat");
        builder.get();
        return builder.build();
    }

    private OkHttpClient createClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeout);
        builder.readTimeout(timeout);
        return builder.build();
    }

    private okhttp3.Call createCall(final OkHttpClient client, final okhttp3.Request request) {
        return client.newCall(request);
    }

    private boolean execute(final okhttp3.Call call) throws IOException {
        try (okhttp3.Response response = call.execute()) {
            final boolean available = response.isSuccessful();
            if (available) {
                logInfo("ChromaDB connection test successful");
            } else {
                logWarn("ChromaDB connection test failed with status: {}", response.code());
            }
            return available;
        }
    }

    private static void logInfo(final String message, final Object... arguments) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(message, arguments);
        }
    }

    private static void logWarn(final String message, final Object... arguments) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(message, arguments);
        }
    }
}
