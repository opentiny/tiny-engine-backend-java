/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.rag.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RAG config
 */
@Component
@ConfigurationProperties(prefix = "rag")
@Data
public class RAGConfig {

    // 文档处理配置
    private int chunkSize = 1000;
    private int chunkOverlap = 200;
    private int maxResults = 10;
    private double minScore = 0.7;

    // Chroma 配置
    private String chromaBaseUrl = System.getenv("CHROMA_BASE_URL");
    private String chromaCollectionName = "tinyengine_documents";
    private String modelPath = System.getenv("MODEL_PATH");
    private String tokenizerPath = System.getenv("TOKENIZER_PATH");

    // 连接配置
    private int timeoutSeconds = 30;
    private int maxRetries = 5;
    private int retryIntervalMs = 1000;

    // 嵌入模型配置
    private String embeddingModel = "all-minilm-l6-v2";
    private int batchSize = 50;

    // 其他配置
    private boolean debugMode = false;
}
