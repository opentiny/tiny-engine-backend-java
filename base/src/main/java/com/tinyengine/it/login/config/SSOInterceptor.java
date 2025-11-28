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

package com.tinyengine.it.login.config;

import com.tinyengine.it.login.utils.JwtUtil;
import com.tinyengine.it.login.config.context.DefaultLoginUserContext;
import com.tinyengine.it.login.model.UserInfo;
import com.tinyengine.it.model.entity.Tenant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * SSO Interceptor
 */
@Slf4j
@Component
public class SSOInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String SSO_SERVER = System.getenv("SSO_SERVER");

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        log.info("Intercepting: {}, Token: {}", requestURI, token != null ? "present" : "null");

        // 如果没有token，重定向到登录页
        if (token == null || token.isEmpty()) {
            log.info("No token, redirecting to: {}", SSO_SERVER);
            response.sendRedirect(SSO_SERVER);
            return false;
        }

        try {
            // 验证token
            if (!jwtUtil.validateToken(token)) {
                log.warn("Token validation failed");
                response.sendRedirect(SSO_SERVER);
                return false;
            }

            // 从token中获取用户信息
            String username = jwtUtil.getUsernameFromToken(token);
            String userId = jwtUtil.getUserIdFromToken(token);
            List<Tenant> tenants = jwtUtil.getTenantIdFromToken(token);
            String roles = jwtUtil.getRolesFromToken(token);
            Integer platformId = jwtUtil.getPlatformIdFromToken(token);


            // 检查必需的用户信息
            if (username == null || username.isEmpty() || userId == null) {
                log.warn("User information is incomplete - username: {}, userId: {}", username, userId);
                response.sendRedirect(SSO_SERVER);
                return false;
            }

            // 存储用户信息到LoginUserContext
            UserInfo userInfo = new UserInfo(userId, username, tenants);

            userInfo.setPlatformId(platformId != null ? platformId : 0);
            userInfo.setRoles(roles != null ? roles : "USER");
            userInfo.setToken(token);

            DefaultLoginUserContext.setCurrentUser(userInfo);

            log.info("Token validated and user context set for user: {}", username);
            return true;

        } catch (Exception e) {
            log.error("Token validation exception: {}", e.getMessage(), e);
            response.sendRedirect(SSO_SERVER);
            DefaultLoginUserContext.clear();
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
        HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理用户上下文
        DefaultLoginUserContext.clear();
        log.debug("Cleared user context for request completion");
    }
}
