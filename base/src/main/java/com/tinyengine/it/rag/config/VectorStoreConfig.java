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
package com.tinyengine.it.rag.config;

import com.tinyengine.it.rag.service.StorageService;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.OnnxEmbeddingModel;
import dev.langchain4j.model.embedding.onnx.PoolingMode;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/** Vector store config */
@Configuration
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.DataflowAnomalyAnalysis"
})
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The configuration is a Spring-managed collaborator and is not exposed.")
public class VectorStoreConfig {
    private static final int CHROMA_TIMEOUT = 30;
    private static final String FALLBACK_WARNING =
            "RAG features are disabled - using fallback embedding store";

    private final RAGConfig ragConfig;
    private final ChromaConnectionProbe connectionProbe;

    /**
     * 嵌入模型 Bean - 尝试创建，失败时返回降级实现.
     *
     * @return embedding model bean
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        EmbeddingModel embeddingModel;
        try {
            // 检查必要的配置参数
            if (ragConfig.getModelPath() == null || ragConfig.getTokenizerPath() == null) {
                logWarn("ONNX model configuration is incomplete, using fallback embedding model");
                embeddingModel = createFallbackEmbeddingModel();
            } else {
                logInfo("Initializing ONNX embedding model...");
                embeddingModel =
                        new OnnxEmbeddingModel(
                                ragConfig.getModelPath(),
                                ragConfig.getTokenizerPath(),
                                PoolingMode.MEAN);
                logInfo("ONNX embedding model initialization successful");
            }
        } catch (IllegalArgumentException  exception) {
            logWarn(
                    "ONNX embedding model initialization failed, using fallback implementation",
                    exception);
            embeddingModel = createFallbackEmbeddingModel();
        }
        return embeddingModel;
    }

    /**
     * 嵌入存储 Bean - 尝试创建，失败时返回降级实现.
     *
     * @return embedding store bean
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        EmbeddingStore<TextSegment> embeddingStore = createFallbackEmbeddingStore();
        final String chromaBaseUrl = ragConfig.getChromaBaseUrl();
        if (chromaBaseUrl == null) {
            logWarn("ChromaDB configuration is incomplete, using fallback embedding store");
        } else if (connectionProbe.isAvailable(chromaBaseUrl)) {
            try {
                logInfo("Attempting to initialize ChromaDB connection: {}", chromaBaseUrl);
                final String collectionName = getCollectionName();
                embeddingStore =
                        ChromaStoreFactory.create(
                                chromaBaseUrl,
                                collectionName,
                                Duration.ofSeconds(CHROMA_TIMEOUT));
                logInfo("ChromaDB embeddingStore initialization successful");
            } catch (IllegalArgumentException exception) {
                logWarn("ChromaDB initialization failed, using fallback embedding store", exception);
            }
        } else {
            logWarn("ChromaDB connection test failed, using fallback embedding store");
        }
        return embeddingStore;
    }

    /**
     * 存储服务 Bean - 总是创建，依赖降级实现.
     *
     * @return storage service bean
     */
    @Bean
    public StorageService vectorStorageService(
            final EmbeddingModel embeddingModel,
            final EmbeddingStore<TextSegment> embeddingStore) {
        StorageService storageService;
        try {
            storageService = new StorageService(embeddingModel, embeddingStore, ragConfig);

            // 检查服务状态
            final boolean modelAvailable = !(embeddingModel instanceof FallbackEmbeddingModel);
            final boolean storeAvailable = !(embeddingStore instanceof FallbackEmbeddingStore);

            if (modelAvailable && storeAvailable) {
                logInfo("StorageService initialization completed - RAG features are fully available");
            } else {
                logWarn(
                        "StorageService initialization completed - RAG features are limited: "
                                + "Model available: {}, Store available: {}",
                        modelAvailable,
                        storeAvailable);
            }

        } catch (IllegalArgumentException exception) {
            logError("StorageService initialization failed, creating fallback instance", exception);
            // 创建完全降级的实例
            storageService = new StorageService(
                    createFallbackEmbeddingModel(), createFallbackEmbeddingStore(), ragConfig);
        }
        return storageService;
    }

    /**
     * 创建降级嵌入模型.
     *
     * @return fallback embedding model
     */
    private EmbeddingModel createFallbackEmbeddingModel() {
        return new FallbackEmbeddingModel();
    }

    /**
     * 创建降级嵌入存储.
     *
     * @return fallback embedding store
     */
    private EmbeddingStore<TextSegment> createFallbackEmbeddingStore() {
        return new FallbackEmbeddingStore();
    }

    private String getCollectionName() {
        final String collectionName = ragConfig.getChromaCollectionName();
        return collectionName == null ? "documents" : collectionName;
    }

    private static void logInfo(final String message, final Object... arguments) {
        if (log.isInfoEnabled()) {
            log.info(message, arguments);
        }
    }

    private static void logWarn(final String message, final Object... arguments) {
        if (log.isWarnEnabled()) {
            log.warn(message, arguments);
        }
    }

    private static void logError(final String message, final Object... arguments) {
        if (log.isErrorEnabled()) {
            log.error(message, arguments);
        }
    }

    private static void logFallbackEmbeddingStoreWarning() {
        logWarn(FALLBACK_WARNING);
    }

    /** 降级嵌入模型实现 */
    private static class FallbackEmbeddingModel implements EmbeddingModel {
        @Override
        public Response<List<Embedding>> embedAll(final List<TextSegment> textSegments) {
            logWarn("RAG features are disabled - using fallback embedding model");
            // 返回空的嵌入列表
            return Response.from(Collections.emptyList());
        }
    }

    /** 降级嵌入存储实现 */
    private static class FallbackEmbeddingStore implements EmbeddingStore<TextSegment> {
        @Override
        public String add(final Embedding embedding) {
            logFallbackEmbeddingStoreWarning();
            return "fallback-id";
        }

        @Override
        public void add(final String identifier, final Embedding embedding) {
            logFallbackEmbeddingStoreWarning();
        }

        @Override
        public String add(final Embedding embedding, final TextSegment embedded) {
            logFallbackEmbeddingStoreWarning();
            return "fallback-id";
        }

        @Override
        public List<String> addAll(final List<Embedding> embeddings) {
            logFallbackEmbeddingStoreWarning();
            return Collections.emptyList();
        }

        @Override
        public List<String> addAll(
                final List<Embedding> embeddings, final List<TextSegment> embedded) {
            logFallbackEmbeddingStoreWarning();
            return Collections.emptyList();
        }

        @Override
        public EmbeddingSearchResult<TextSegment> search(final EmbeddingSearchRequest request) {
            logFallbackEmbeddingStoreWarning();
            return new EmbeddingSearchResult<>(Collections.emptyList());
        }
    }

}
