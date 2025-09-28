package com.tinyengine.it.rag.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RAG 配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "rag.config")
public class RAGConfig {

    // 文档处理配置
    public static int CHUNK_SIZE = 1000;
    public static int CHUNK_OVERLAP = 200;
    public static int MAX_RESULTS = 5;
    public static double MIN_SCORE = 0.7;

    // Chroma 配置
    private String chromaBaseUrl = "http://localhost:8000";
    private String chromaCollectionName = "tinyengine_documents";

    // 连接配置
    private int timeoutSeconds = 30;
    private int maxRetries = 5;
    private int retryIntervalMs = 1000;

    // 嵌入模型配置
    private String embeddingModel = "all-minilm-l6-v2";
    private int batchSize = 50;

    // 其他配置
    private boolean debugMode = false;

    // Getter 和 Setter 方法
    public static int getChunkSize() { return CHUNK_SIZE; }
    public static void setChunkSize(int chunkSize) { CHUNK_SIZE = chunkSize; }

    public static int getChunkOverlap() { return CHUNK_OVERLAP; }
    public static void setChunkOverlap(int chunkOverlap) { CHUNK_OVERLAP = chunkOverlap; }

    public static int getMaxResults() { return MAX_RESULTS; }
    public static void setMaxResults(int maxResults) { MAX_RESULTS = maxResults; }

    public static double getMinScore() { return MIN_SCORE; }
    public static void setMinScore(double minScore) { MIN_SCORE = minScore; }
}
