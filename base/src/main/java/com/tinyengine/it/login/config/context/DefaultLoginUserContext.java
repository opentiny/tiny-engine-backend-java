package com.tinyengine.it.login.config.context;

import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.login.model.UserInfo;
import org.springframework.stereotype.Component;

/**
 * 默认登录用户上下文实现
 */
@Component
public class DefaultLoginUserContext implements LoginUserContext {

    private static final ThreadLocal<UserInfo> currentUser = new ThreadLocal<>();

    @Override
    public String getTenantId() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getTenantId() : null;
    }

    @Override
    public String getLoginUserId() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    @Override
    public String getRenterId() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getRenterId() : null;
    }

    @Override
    public int getPlatformId() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getPlatformId() : 0;
    }

    @Override
    public String getSiteId() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getSiteId() : null;
    }

    /**
     * 设置当前用户信息
     */
    public static void setCurrentUser(UserInfo userInfo) {
        currentUser.set(userInfo);
    }

    /**
     * 获取当前用户完整信息
     */
    public static UserInfo getCurrentUser() {
        return currentUser.get();
    }

    /**
     * 清理用户信息
     */
    public static void clear() {
        currentUser.remove();
    }


}

