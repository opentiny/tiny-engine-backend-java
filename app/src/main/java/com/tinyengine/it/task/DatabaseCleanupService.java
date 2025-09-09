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

package com.tinyengine.it.task;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Service
public class DatabaseCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseCleanupService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CleanupProperties cleanupProperties;
    // 执行统计
    private final Map<String, ExecutionStats> executionStats = new ConcurrentHashMap<>();
    private final AtomicInteger totalExecutions = new AtomicInteger(0);

    // 默认白名单表（如果配置文件未设置）
    private static final List<String> DEFAULT_TABLES = Arrays.asList(
        "t_resource", "t_resource_group", "r_resource_group_resource", "t_app_extension",
        "t_block", "t_block_carriers_relation", "t_block_group", "t_block_history",
        "r_material_block", "r_material_history_block", "r_block_group_block", "t_datasource",
        "t_i18n_entry", "t_model", "t_page", "t_page_history", "t_page_template"
    );

    /**
     * 每天24:00自动执行清空操作
     */
    @Scheduled(cron = "${cleanup.cron-expression:0 0 0 * * ?}")
    public void autoCleanupAtMidnight() {
        if (!cleanupProperties.isEnabled()) {
            logger.info("⏸️  清空任务已禁用，跳过执行");
            return;
        }

        String executionId = UUID.randomUUID().toString().substring(0, 8);
        String startTime = LocalDateTime.now().format(formatter);

        logger.info("🎯 ======= 开始执行数据库清空任务 [{}] =======", executionId);
        logger.info("⏰ 执行时间: {}", startTime);
        logger.info("📋 目标表: {}", getWhitelistTables());

        ExecutionStats stats = new ExecutionStats(executionId, startTime);
        executionStats.put(executionId, stats);
        totalExecutions.incrementAndGet();

        int successCount = 0;
        int failedCount = 0;
        long totalRowsCleaned = 0;

        for (String tableName : getWhitelistTables()) {
            try {
                validateTableName(tableName);

                if (!tableExists(tableName)) {
                    logger.warn("⚠️  表 {} 不存在，跳过", tableName);
                    stats.recordSkipped(tableName, "表不存在");
                    continue;
                }

                long beforeCount = getTableRecordCount(tableName);
                long rowsCleaned;

                if (cleanupProperties.isUseTruncate()) {
                    truncateTable(tableName);
                    rowsCleaned = beforeCount; // TRUNCATE会清空所有数据
                } else {
                    rowsCleaned = clearTableData(tableName);
                }

                totalRowsCleaned += rowsCleaned;
                successCount++;

                logger.info("✅ 表 {} 清空完成: 删除 {} 条记录", tableName, rowsCleaned);
                stats.recordSuccess(tableName, rowsCleaned);

            } catch (Exception e) {
                failedCount++;
                logger.error("❌ 表 {} 清空失败: {}", tableName, e.getMessage(), e);
                stats.recordFailure(tableName, e.getMessage());
            }
        }

        String endTime = LocalDateTime.now().format(formatter);
        stats.setEndTime(endTime);
        stats.setTotalRowsCleaned(totalRowsCleaned);

        logger.info("📊 ======= 任务完成统计 [{}] =======", executionId);
        logger.info("✅ 成功表数: {}", successCount);
        logger.info("❌ 失败表数: {}", failedCount);
        logger.info("📈 总共删除记录: {}", totalRowsCleaned);
        logger.info("⏰ 耗时: {} 秒", stats.getDurationSeconds());
        logger.info("🕐 开始: {}, 结束: {}", startTime, endTime);
        logger.info("🎉 ======= 任务执行完成 =======\n");
    }

    /**
     * 每天23:55发送预警通知
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void sendCleanupWarning() {
        if (!cleanupProperties.isEnabled() || !cleanupProperties.isSendWarning()) {
            return;
        }

        logger.warn("⚠️  ⚠️  ⚠️  重要通知：5分钟后将自动清空数据库表！");
        logger.warn("📋 目标表: {}", getWhitelistTables());
        logger.warn("⏰ 执行时间: 00:00:00");
        logger.warn("💡 如需取消，请修改配置: cleanup.enabled=false");
        logger.warn("==========================================");
    }

    /**
     * 应用启动时初始化
     */
    @PostConstruct
    public void init() {
        logger.info("🚀 数据库自动清空服务初始化完成");
        logger.info("📋 配置表: {}", getWhitelistTables());
        logger.info("⏰ 执行时间: {}", cleanupProperties.getCronExpression());
        logger.info("🔧 使用模式: {}", cleanupProperties.isUseTruncate() ? "TRUNCATE" : "DELETE");
        logger.info("✅ 服务状态: {}", cleanupProperties.isEnabled() ? "已启用" : "已禁用");
        logger.info("==========================================");
    }

    /**
     * 获取白名单表列表
     */
    public List<String> getWhitelistTables() {
        List<String> tables = cleanupProperties.getWhitelistTables();
        return tables != null && !tables.isEmpty() ? tables : DEFAULT_TABLES;
    }

    /**
     * 清空表数据（DELETE方式）
     */
    private long clearTableData(String tableName) {
        validateTableName(tableName);
        String sql = "DELETE FROM " + tableName;
        int affectedRows = jdbcTemplate.update(sql);
        return affectedRows;
    }

    /**
     * 清空表数据（TRUNCATE方式）
     */
    private void truncateTable(String tableName) {
        validateTableName(tableName);
        String sql = "TRUNCATE TABLE " + tableName;
        jdbcTemplate.execute(sql);
    }

    /**
     * 检查表是否存在
     */
    public boolean tableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.tables " +
                    "WHERE table_schema = DATABASE() AND table_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName.toUpperCase());
            return count != null && count > 0;
        } catch (Exception e) {
            logger.warn("检查表存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取表记录数量
     */
    public long getTableRecordCount(String tableName) {
        try {
            validateTableName(tableName);
            String sql = "SELECT COUNT(*) FROM " + tableName;
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            logger.error("获取表记录数失败: {}", e.getMessage());
            return -1;
        }
    }

    /**
     * 验证表名安全性
     */
    private void validateTableName(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("表名不能为空");
        }
        if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("无效的表名格式: " + tableName);
        }
    }

    /**
     * 获取执行统计
     */
    public Map<String, ExecutionStats> getExecutionStats() {
        return new LinkedHashMap<>(executionStats);
    }

    public int getTotalExecutions() {
        return totalExecutions.get();
    }

    /**
     * 执行统计内部类
     */
    public static class ExecutionStats {
        private final String executionId;
        private final String startTime;
        private String endTime;
        private long totalRowsCleaned;
        private final Map<String, TableResult> tableResults = new LinkedHashMap<>();

        public ExecutionStats(String executionId, String startTime) {
            this.executionId = executionId;
            this.startTime = startTime;
        }

        public void recordSuccess(String tableName, long rowsCleaned) {
            tableResults.put(tableName, new TableResult("SUCCESS", rowsCleaned, null));
        }

        public void recordFailure(String tableName, String errorMessage) {
            tableResults.put(tableName, new TableResult("FAILED", 0, errorMessage));
        }

        public void recordSkipped(String tableName, String reason) {
            tableResults.put(tableName, new TableResult("SKIPPED", 0, reason));
        }

        // Getters and setters
        public String getExecutionId() { return executionId; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
        public long getTotalRowsCleaned() { return totalRowsCleaned; }
        public void setTotalRowsCleaned(long totalRowsCleaned) { this.totalRowsCleaned = totalRowsCleaned; }
        public Map<String, TableResult> getTableResults() { return tableResults; }

        public long getDurationSeconds() {
            if (startTime != null && endTime != null) {
                LocalDateTime start = LocalDateTime.parse(startTime, formatter);
                LocalDateTime end = LocalDateTime.parse(endTime, formatter);
                return java.time.Duration.between(start, end).getSeconds();
            }
            return 0;
        }
    }

    /**
     * 表结果内部类
     */
    public static class TableResult {
        private final String status;
        private final long rowsCleaned;
        private final String message;

        public TableResult(String status, long rowsCleaned, String message) {
            this.status = status;
            this.rowsCleaned = rowsCleaned;
            this.message = message;
        }

        // Getters
        public String getStatus() { return status; }
        public long getRowsCleaned() { return rowsCleaned; }
        public String getMessage() { return message; }
    }
}
