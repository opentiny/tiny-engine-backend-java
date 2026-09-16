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

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** RAG config */
@Component
@ConfigurationProperties(prefix = "rag")
@Data
public class RAGConfig {
    private static final int DEF_CHUNK_SIZE = 1000;
    private static final int DEF_CHUNK_OVER = 200;
    private static final int DEF_MAX_RESULTS = 10;
    private static final double DEF_MIN_SCORE = 0.7;
    private static final int DEF_TIMEOUT_SEC = 30;
    private static final int DEF_MAX_RETRIES = 5;
    private static final int DEF_RETRY_MS = 1000;
    private static final int DEF_BATCH_SIZE = 50;

    // 文档处理配置
    private int chunkSize = DEF_CHUNK_SIZE;
    private int chunkOverlap = DEF_CHUNK_OVER;
    private int maxResults = DEF_MAX_RESULTS;
    private double minScore = DEF_MIN_SCORE;

    // Chroma 配置
    private String chromaBaseUrl = System.getenv("CHROMA_BASE_URL");
    @SuppressWarnings("PMD.LongVariable")
    private String chromaCollectionName = "tinyengine_documents";
    private String modelPath = System.getenv("MODEL_PATH");
    private String tokenizerPath = System.getenv("TOKENIZER_PATH");
    private String documentRoot = System.getenv("FOLDER_PATH");

    // 连接配置
    private int timeoutSeconds = DEF_TIMEOUT_SEC;
    private int maxRetries = DEF_MAX_RETRIES;
    private int retryIntervalMs = DEF_RETRY_MS;

    // 嵌入模型配置
    private String embeddingModel = "all-minilm-l6-v2";
    private int batchSize = DEF_BATCH_SIZE;

    // 其他配置
    private boolean debugMode;
}
