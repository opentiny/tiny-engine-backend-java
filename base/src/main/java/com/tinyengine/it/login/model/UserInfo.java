package com.tinyengine.it.login.model;

/**
 * 用户信息内部类
 */
public class UserInfo {
    private String userId;
    private String username;
    private String tenantId;
    private String renterId;
    private int platformId;
    private String siteId;
    private String roles;
    private String token;
    private String department;

    // 构造器
    public UserInfo(String userId, String username, String tenantId) {
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
    }

    // getter和setter方法
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getRenterId() { return renterId; }
    public void setRenterId(String renterId) { this.renterId = renterId; }
    public int getPlatformId() { return platformId; }
    public void setPlatformId(int platformId) { this.platformId = platformId; }
    public String getSiteId() { return siteId; }
    public void setSiteId(String siteId) { this.siteId = siteId; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}