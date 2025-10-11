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

package com.tinyengine.it.rag.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vector document dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VectorDocument {

    /**
     * 成功处理的文档数量
     */
    private int successCount;

    /**
     * 处理失败的文档数量
     */
    private int errorCount;

    /**
     * 总处理时间（毫秒）
     */
    private long processingTime;

    /**
     * 文档集ID（可选）
     */
    private String documentSetId;

    /**
     * 文档集集合
     */
    private String collectionName;

    /**
     * 处理状态
     */
    private String status;

    public VectorDocument(int successCount, int errorCount) {
        this.successCount = successCount;
        this.errorCount = errorCount;
        this.processingTime = 0L;
        this.status = errorCount == 0 ? "SUCCESS" : "PARTIAL_SUCCESS";
    }

    public VectorDocument(int successCount, int errorCount, long processingTime) {
        this.successCount = successCount;
        this.errorCount = errorCount;
        this.processingTime = processingTime;
        this.status = errorCount == 0 ? "SUCCESS" : "PARTIAL_SUCCESS";
    }
    public VectorDocument(int successCount, int errorCount, String documentSetId, String collectionName) {
        this.successCount = successCount;
        this.errorCount = errorCount;
        this.documentSetId = documentSetId;
        this.collectionName = collectionName;
        this.processingTime = 0L;
        this.status = errorCount == 0 ? "SUCCESS" : "PARTIAL_SUCCESS";
    }
    /**
     * 获取总处理数量
     */
    public int getTotalCount() {
        return successCount + errorCount;
    }

    /**
     * 获取成功率
     */
    public double getSuccessRate() {
        int total = getTotalCount();
        return total > 0 ? (double) successCount / total * 100 : 0.0;
    }
}
