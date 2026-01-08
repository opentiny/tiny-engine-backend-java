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

    private static final ThreadLocal<UserInfo> CURRENT_USER = new ThreadLocal<>();

    private static final int DEFAULT_PLATFORM = 1;
    private static final String DEFAULT_TENANT = "1";



    /**
     * 返回当前用户所在的业务租户id
     *
     * @return 租户Id
     */
    @Override
    public String getTenantId() {
        UserInfo userInfo = CURRENT_USER.get();
        List<Tenant> tenantList = userInfo != null ? userInfo.getTenants() : null;
        if (tenantList == null || tenantList.isEmpty()) {
            return DEFAULT_TENANT;
        }
        for (Tenant tenant : tenantList) {
            if(tenant.getIsInUse()!=null){
                if (tenant.getIsInUse()) {
                    return tenant.getId();
                }
            }else{
                return tenantList.get(0).getId();
            }

        }
        return DEFAULT_TENANT;
    }

    @Override
    public List<Tenant> getTenants() {
        UserInfo userInfo = CURRENT_USER.get();
        return userInfo != null ? userInfo.getTenants() : null;
    }

    @Override
    public String getLoginUserId() {
        UserInfo userInfo = CURRENT_USER.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    @Override
    public int getPlatformId() {
        UserInfo userInfo = CURRENT_USER.get();
        return userInfo != null ? userInfo.getPlatformId() : DEFAULT_PLATFORM;
    }

    /**
     * 设置当前组织信息
     *
     * @param tenants
     */
    @Override
    public void setTenants(List<Tenant> tenants) {
        UserInfo userInfo = CURRENT_USER.get();
        userInfo.setTenants(tenants);
        CURRENT_USER.set(userInfo);
    }


    /**
     * 设置当前用户信息
     */
    public static void setCurrentUser(UserInfo userInfo) {

        CURRENT_USER.set(userInfo);
    }

    /**
     * 获取当前用户完整信息
     */
    public static UserInfo getCurrentUser() {

        return CURRENT_USER.get();
    }

    /**
     * 清理用户信息
     */
    public static void clear() {

        CURRENT_USER.remove();
    }


}

