/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.rag.config;

import com.tinyengine.it.rag.service.StorageService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.OnnxEmbeddingModel;
import dev.langchain4j.model.embedding.onnx.PoolingMode;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Vector store config
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class VectorStoreConfig {

    private final RAGConfig ragConfig;

    /**
     * 嵌入模型 Bean - 尝试创建，失败时返回降级实现
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        try {
            // 检查必要的配置参数
            if (ragConfig.getModelPath() == null || ragConfig.getTokenizerPath() == null) {
                log.warn("ONNX model configuration is incomplete, using fallback embedding model");
                return createFallbackEmbeddingModel();
            }

            log.info("Initializing ONNX embedding model...");

            EmbeddingModel model = new OnnxEmbeddingModel(
                ragConfig.getModelPath(),
                ragConfig.getTokenizerPath(),
                PoolingMode.MEAN
            );

            log.info("✅ ONNX embedding model initialization successful");
            return model;

        } catch (Exception e) {
            log.warn("❌ ONNX embedding model initialization failed, using fallback implementation", e);
            return createFallbackEmbeddingModel();
        }
    }

    /**
     * 嵌入存储 Bean - 尝试创建，失败时返回降级实现
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        try {
            // 检查必要的配置参数
            if (ragConfig.getChromaBaseUrl() == null) {
                log.warn("ChromaDB configuration is incomplete, using fallback embedding store");
                return createFallbackEmbeddingStore();
            }

            log.info("Attempting to initialize ChromaDB connection: {}", ragConfig.getChromaBaseUrl());

            // 测试连接
            if (!testChromaConnection(ragConfig.getChromaBaseUrl())) {
                log.warn("ChromaDB connection test failed, using fallback embedding store");
                return createFallbackEmbeddingStore();
            }

            ChromaEmbeddingStore embeddingStore = ChromaEmbeddingStore.builder()
                .baseUrl(ragConfig.getChromaBaseUrl())
                .collectionName(ragConfig.getChromaCollectionName() != null ?
                    ragConfig.getChromaCollectionName() : "documents")
                .timeout(Duration.ofSeconds(30))
                .build();

            log.info("✅ ChromaDB embeddingStore initialization successful");
            return embeddingStore;

        } catch (Exception e) {
            log.warn("❌ ChromaDB initialization failed, using fallback embedding store", e);
            return createFallbackEmbeddingStore();
        }
    }

    /**
     * 存储服务 Bean - 总是创建，依赖降级实现
     */
    @Bean
    public StorageService vectorStorageService(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        try {
            StorageService service = new StorageService(embeddingModel, embeddingStore);

            // 检查服务状态
            boolean modelAvailable = !(embeddingModel instanceof FallbackEmbeddingModel);
            boolean storeAvailable = !(embeddingStore instanceof FallbackEmbeddingStore);

            if (modelAvailable && storeAvailable) {
                log.info("✅ StorageService initialization completed - RAG features are fully available");
            } else {
                log.warn("⚠️ StorageService initialization completed - RAG features are limited: " +
                    "Model available: {}, Store available: {}", modelAvailable, storeAvailable);
            }

            return service;

        } catch (Exception e) {
            log.error("❌ StorageService initialization failed, creating fallback instance", e);
            // 创建完全降级的实例
            return new StorageService(createFallbackEmbeddingModel(), createFallbackEmbeddingStore());
        }
    }

    /**
     * 测试 ChromaDB 连接 - 返回布尔值而不是抛出异常
     */
    private boolean testChromaConnection(String baseUrl) {
        try {
            okhttp3.Request request = new okhttp3.Request.Builder()
                .url(baseUrl + "/api/v1/heartbeat")
                .get()
                .build();

            OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(5))
                .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    log.info("✅ ChromaDB connection test successful");
                    return true;
                } else {
                    log.warn("ChromaDB connection test failed with status: {}", response.code());
                    return false;
                }
            }
        } catch (Exception e) {
            log.warn("ChromaDB connection test failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建降级嵌入模型
     */
    private EmbeddingModel createFallbackEmbeddingModel() {
        return new FallbackEmbeddingModel();
    }

    /**
     * 创建降级嵌入存储
     */
    private EmbeddingStore<TextSegment> createFallbackEmbeddingStore() {
        return new FallbackEmbeddingStore();
    }

    /**
     * 降级嵌入模型实现
     */
    private static class FallbackEmbeddingModel implements EmbeddingModel {
        @Override
        public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
            log.warn("RAG features are disabled - using fallback embedding model");
            // 返回空的嵌入列表
            return Response.from(Collections.emptyList());
        }
    }

    /**
     * 降级嵌入存储实现
     */
    private static class FallbackEmbeddingStore implements EmbeddingStore<TextSegment> {
        @Override
        public String add(Embedding embedding) {
            log.warn("RAG features are disabled - using fallback embedding store");
            return "fallback-id";
        }

        @Override
        public void add(String id, Embedding embedding) {
            log.warn("RAG features are disabled - using fallback embedding store");
        }

        @Override
        public String add(Embedding embedding, TextSegment embedded) {
            log.warn("RAG features are disabled - using fallback embedding store");
            return "fallback-id";
        }

        @Override
        public List<String> addAll(List<Embedding> embeddings) {
            log.warn("RAG features are disabled - using fallback embedding store");
            return Collections.emptyList();
        }

        @Override
        public List<String> addAll(List<Embedding> embeddings, List<TextSegment> embedded) {
            log.warn("RAG features are disabled - using fallback embedding store");
            return Collections.emptyList();
        }

        @Override
        public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
            log.warn("RAG features are disabled - using fallback embedding store");
            return new EmbeddingSearchResult<>(Collections.emptyList());
        }
    }
}
