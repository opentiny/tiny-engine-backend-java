package com.tinyengine.it.login.config.context;

import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.login.model.UserInfo;
import com.tinyengine.it.model.entity.Tenant;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认登录用户上下文实现
 */
@Component
public class DefaultLoginUserContext implements LoginUserContext {

    private static final ThreadLocal<UserInfo> currentUser = new ThreadLocal<>();

    private static final int DEFAULT_PLATFORM = 1;

    @Override
    public List<Tenant> getTenants() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getTenants() : null;
    }

    @Override
    public String getLoginUserId() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    @Override
    public int getPlatformId() {
        UserInfo userInfo = currentUser.get();
        return userInfo != null ? userInfo.getPlatformId() : DEFAULT_PLATFORM;
    }

    /**
     * 设置当前组织信息
     *
     * @param tenants
     */
    @Override
    public void setTenants(List<Tenant> tenants) {
        UserInfo userInfo = currentUser.get();
        userInfo.setTenants(tenants);
        currentUser.set(userInfo);
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

