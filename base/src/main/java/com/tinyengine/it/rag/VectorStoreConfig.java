package com.tinyengine.it.rag;

import com.tinyengine.it.rag.entity.RAGConfig;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class VectorStoreConfig {

    private final RAGConfig ragConfig;

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder().apiKey("").build();
    }
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        log.info("正在初始化 ChromaDB 连接: {}", ragConfig.getChromaBaseUrl());

        try {
            // 先测试连接
            testChromaConnection(ragConfig.getChromaBaseUrl());

            // 创建自定义的 HTTP 客户端，增加超时时间
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Duration.ofSeconds(30))
                    .readTimeout(Duration.ofSeconds(30))
                    .writeTimeout(Duration.ofSeconds(30))
                    .build();

            ChromaEmbeddingStore embeddingStore = ChromaEmbeddingStore.builder()
                    .baseUrl(ragConfig.getChromaBaseUrl())
                    .collectionName(ragConfig.getChromaCollectionName())
                    .timeout(Duration.ofSeconds(30))
                    .build();

            log.info("✅ ChromaDB EmbeddingStore 初始化成功");
            return embeddingStore;

        } catch (Exception e) {
            log.error("❌ ChromaDB 初始化失败", e);
            throw new RuntimeException("无法初始化 ChromaDB 存储", e);
        }
    }

    private void testChromaConnection(String baseUrl) {
        try {
            // 简单的连接测试
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(baseUrl + "/api/v1/heartbeat")
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient();
            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    log.info("✅ ChromaDB 连接测试成功");
                } else {
                    throw new RuntimeException("ChromaDB 连接测试失败: HTTP " + response.code());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("无法连接到 ChromaDB: " + e.getMessage(), e);
        }
    }

    @Bean
    public VectorStorageService vectorStorageService(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        return new VectorStorageService(embeddingModel,embeddingStore);
    }
}
