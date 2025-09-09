package com.tinyengine.it.task;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;
@Configuration
@ConfigurationProperties(prefix = "cleanup")
public class CleanupProperties {

    private boolean enabled = true;
    private boolean useTruncate = false;
    private List<String> whitelistTables;
    private String cronExpression = "0 0 0 * * ?";
    private boolean sendWarning = true;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isUseTruncate() { return useTruncate; }
    public void setUseTruncate(boolean useTruncate) { this.useTruncate = useTruncate; }

    public List<String> getWhitelistTables() { return whitelistTables; }
    public void setWhitelistTables(List<String> whitelistTables) { this.whitelistTables = whitelistTables; }

    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }

    public boolean isSendWarning() { return sendWarning; }
    public void setSendWarning(boolean sendWarning) { this.sendWarning = sendWarning; }
}