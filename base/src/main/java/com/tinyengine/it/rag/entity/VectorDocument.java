package com.tinyengine.it.rag.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 向量文档处理结果实体类 - 需要自己编写
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
