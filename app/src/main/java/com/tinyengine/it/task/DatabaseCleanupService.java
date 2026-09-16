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
package com.tinyengine.it.task;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static java.time.Duration.between;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@SuppressWarnings({"PMD.TooManyMethods", "PMD.DataflowAnomalyAnalysis"})
public class DatabaseCleanupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseCleanupService.class);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int EXEC_ID_LENGTH = 8;
    private static final int HEX_NIBBLE_SHIFT = 4;
    private static final int HEX_NIBBLE_MASK = 0x0F;
    private static final int HEX_RADIX = 16;
    private static final SecureRandom ID_RANDOM = new SecureRandom();

    private final boolean cleanupEnabled;
    private final boolean useTruncate;
    private final boolean sendWarning;
    private final String cronExpression;
    private final List<String> whitelistTables;
    private final JdbcTemplate jdbcTemplate;

    private final Map<String, ExecutionStats> executionStats = new ConcurrentHashMap<>();

    private final AtomicInteger totalExecutions = new AtomicInteger(0);

    // 默认白名单表（如果配置文件未设置）
    private static final List<String> DEFAULT_TABLES =
            Arrays.asList(
                    "t_resource",
                    "t_resource_group",
                    "r_resource_group_resource",
                    "t_app_extension",
                    "t_block",
                    "t_block_carriers_relation",
                    "t_block_group",
                    "t_block_history",
                    "r_material_block",
                    "r_material_history_block",
                    "r_block_group_block",
                    "t_datasource",
                    "t_i18n_entry",
                    "t_model",
                    "t_page",
                    "t_page_history",
                    "t_page_template");

    @Autowired
    public DatabaseCleanupService(
            final JdbcTemplate jdbcTemplate, final CleanupProperties cleanupProperties) {
        this.jdbcTemplate = jdbcTemplate;
        cleanupEnabled = cleanupProperties.isEnabled();
        useTruncate = cleanupProperties.isUseTruncate();
        sendWarning = cleanupProperties.isSendWarning();
        cronExpression = cleanupProperties.getCronExpression();
        final List<String> configuredTables = cleanupProperties.getWhitelistTables();
        whitelistTables =
                configuredTables == null || configuredTables.isEmpty()
                        ? DEFAULT_TABLES
                        : List.copyOf(configuredTables);
    }

    /** 每天24:00自动执行清空操作 */
    @Scheduled(cron = "${cleanup.cron-expression:0 0 0 * * ?}")
    public void autoCleanupAtMidnight() {
        if (!cleanupEnabled) {
            logInfo("⏸️ Clearing tasks is disabled, skipping execution");
            return;
        }

        final String executionId = createExecutionId();
        final String startTime = currentTime();

        logInfo("======= Start executing the database clearing task [{}] =======", executionId);
        logInfo("⏰ Time: {}", startTime);
        logInfo("📋 Tables: {}", getWhitelistTables());

        final ExecutionStats stats = new ExecutionStats(executionId, startTime);
        executionStats.put(executionId, stats);
        totalExecutions.incrementAndGet();

        final CleanupSummary cleanupSummary = new CleanupSummary();

        for (final String tableName : getWhitelistTables()) {
            cleanTable(tableName, stats, cleanupSummary);
        }

        final String endTime = currentTime();
        stats.setEndTime(endTime);
        stats.setTotalRowsCleaned(cleanupSummary.getTotalRowsCleaned());

        logInfo("📊 ======= Task Completion Statistics [{}] =======", executionId);
        logInfo("✅ Successful table count: {}", cleanupSummary.getSuccessCount());
        logInfo("❌ Failure count: {}", cleanupSummary.getFailedCount());
        logInfo("📈 Total deleted records: {}", cleanupSummary.getTotalRowsCleaned());
        logInfo("⏰ Time-consuming: {} second", stats.getDurationSeconds());
        logInfo("🕐 Start: {}, End: {}", startTime, endTime);
        logInfo("🎉 ======= Task execution completed =======\n");
    }

    /** 每天23:55发送预警通知 */
    @Scheduled(cron = "0 55 23 * * ?")
    public void sendCleanupWarning() {
        if (!cleanupEnabled || !sendWarning) {
            return;
        }

        logWarn(
                "⚠️  ⚠️  ⚠️ Important Notice: The database table will be automatically cleared in 5"
                        + " minutes！");
        logWarn("📋 Target table: {}", getWhitelistTables());
        logWarn("⏰ Execution Time: 00:00:00");
        logWarn("💡 If you need to cancel, please change the settings: cleanup.enabled=false");
        logWarn("==========================================");
    }

    /** 应用启动时初始化 */
    @PostConstruct
    public void init() {
        logInfo("🚀 Database auto-clear service initialization completed");
        logInfo("📋 Configuration table: {}", getWhitelistTables());
        logInfo("⏰ Execution time: {}", cronExpression);
        logInfo("🔧 Mode in use: {}", useTruncate ? "TRUNCATE" : "DELETE");
        logInfo("✅ Service status: {}", cleanupEnabled ? "Enabled" : "Disabled");
        logInfo("==========================================");
    }


    /**
     * 获取白名单表列表.
     *
     * @return whitelist table names
     */
    public List<String> getWhitelistTables() {
        return List.copyOf(whitelistTables);
    }
    private static String createExecutionId() {
        final byte[] randomBytes = new byte[(EXEC_ID_LENGTH + 1) / 2];
        ID_RANDOM.nextBytes(randomBytes);
        final StringBuilder identifier = new StringBuilder(randomBytes.length * 2);
        for (final byte randomByte : randomBytes) {
            identifier.append(
                    Character.forDigit(
                            (randomByte >>> HEX_NIBBLE_SHIFT) & HEX_NIBBLE_MASK, HEX_RADIX));
            identifier.append(Character.forDigit(randomByte & HEX_NIBBLE_MASK, HEX_RADIX));
        }
        return identifier.substring(0, EXEC_ID_LENGTH);
    }

    private static String currentTime() {
        final ZoneId systemZone = ZoneId.systemDefault();
        final LocalDateTime currentDateTime = LocalDateTime.now(systemZone);
        return FORMATTER.format(currentDateTime);  // 调用静态常量，传入参数
    }

    private void cleanTable(
            final String tableName,
            final ExecutionStats stats,
            final CleanupSummary cleanupSummary) {
        try {
            if (!tableExists(tableName)) {
                logWarn("⚠️  Table {} does not exist, skip", tableName);
                stats.recordSkipped(tableName, "Table does not exist");
                return;
            }

            final long rowsCleaned = clearTable(tableName);
            cleanupSummary.recordSuccess(rowsCleaned);
            logInfo("✅ Table {} cleared: {} records deleted", tableName, rowsCleaned);
            stats.recordSuccess(tableName, rowsCleaned);
        } catch (DataAccessException | IllegalArgumentException exception) {
            cleanupSummary.recordFailure();
            logError(
                    "❌ Failed to clear table {}: {}",
                    tableName,
                    exception.getMessage(),
                    exception);
            stats.recordFailure(tableName, exception.getMessage());
        }
    }
    /**
     * 获取执行统计.
     *
     * @return execution statistics
     */
    public Map<String, ExecutionStats> getExecutionStats() {
        return new LinkedHashMap<>(executionStats);
    }

    public int getTotalExecutions() {
        return totalExecutions.get();
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

    private static void logError(final String message, final Object... arguments) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(message, arguments);
        }
    }

    private static final class CleanupSummary {
        private int successCount;
        private int failedCount;
        private long totalRowsCleaned;

        private void recordSuccess(final long rowsCleaned) {
            successCount++;
            totalRowsCleaned += rowsCleaned;
        }

        private void recordFailure() {
            failedCount++;
        }

        private int getSuccessCount() {
            return successCount;
        }

        private int getFailedCount() {
            return failedCount;
        }

        private long getTotalRowsCleaned() {
            return totalRowsCleaned;
        }
    }

    /** 执行统计内部类 */
    public static class ExecutionStats {
        private final String executionId;
        private final String startTime;
        private String endTime;
        private long totalRowsCleaned;
        private final Map<String, TableResult> tableResults = new LinkedHashMap<>();

        public ExecutionStats(final String executionId, final String startTime) {
            this.executionId = executionId;
            this.startTime = startTime;
        }

        public void recordSuccess(final String tableName, final long rowsCleaned) {
            tableResults.put(tableName, new TableResult("SUCCESS", rowsCleaned, null));
        }

        public void recordFailure(final String tableName, final String errorMessage) {
            tableResults.put(tableName, new TableResult("FAILED", 0, errorMessage));
        }

        public void recordSkipped(final String tableName, final String reason) {
            tableResults.put(tableName, new TableResult("SKIPPED", 0, reason));
        }

        // Getters and setters
        public String getExecutionId() {
            return executionId;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(final String endTime) {
            this.endTime = endTime;
        }

        public long getTotalRowsCleaned() {
            return totalRowsCleaned;
        }

        public void setTotalRowsCleaned(final long totalRowsCleaned) {
            this.totalRowsCleaned = totalRowsCleaned;
        }

        public Map<String, TableResult> getTableResults() {
            return tableResults;
        }
        public long getDurationSeconds() {
            long result = 0;  // 默认值对应 startTime 或 endTime 为空的情况
            if (startTime != null && endTime != null) {
                final LocalDateTime start = LocalDateTime.parse(startTime, FORMATTER);
                final LocalDateTime end = LocalDateTime.parse(endTime, FORMATTER);
                final Duration duration = between(start, end);   // 使用静态导入的方法
                result = durationInSeconds(duration);
            }
            return result;  // 唯一的退出点
        }

        private static long durationInSeconds(final Duration duration) {
            return duration.getSeconds();
        }
    }

    private long clearTable(final String tableName) {
        validateTableName(tableName);
        long rowsCleaned;
        if (useTruncate) {
            final long recordCount = getTableRecordCount(tableName);
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
            rowsCleaned = recordCount;
        } else {
            rowsCleaned = jdbcTemplate.update("DELETE FROM " + tableName);
        }
        return rowsCleaned;
    }

    public boolean tableExists(final String tableName) {
        boolean exists = false;
        try {
            final String sql =
                    "SELECT COUNT(*) FROM information_schema.tables "
                            + "WHERE table_schema = DATABASE() AND table_name = ?";
            final Integer count =
                    jdbcTemplate.queryForObject(
                            sql, Integer.class, tableName.toUpperCase(Locale.ROOT));
            exists = count != null && count > 0;
        } catch (DataAccessException | IllegalArgumentException exception) {
            logWarn("The checklist has failed: {}", exception.getMessage());
        }
        return exists;
    }

    public long getTableRecordCount(final String tableName) {
        long count = -1L;
        try {
            validateTableName(tableName);
            final Long queriedCount =
                    jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Long.class);
            count = queriedCount == null ? 0L : queriedCount;
        } catch (DataAccessException | IllegalArgumentException exception) {
            logError("Table record count failed: {}", exception.getMessage());
        }
        return count;
    }

    private void validateTableName(final String tableName) {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("Table name cannot be empty");
        }
        if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("Invalid table name format: " + tableName);
        }
    }

    /** 表结果内部类 */
    public static class TableResult {
        private final String status;
        private final long rowsCleaned;
        private final String message;

        public TableResult(final String status, final long rowsCleaned, final String message) {
            this.status = status;
            this.rowsCleaned = rowsCleaned;
            this.message = message;
        }

        // Getters
        public String getStatus() {
            return status;
        }

        public long getRowsCleaned() {
            return rowsCleaned;
        }

        public String getMessage() {
            return message;
        }
    }
}
