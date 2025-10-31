package com.tinyengine.it.login.config;

import com.tinyengine.it.login.Utils.JwtUtil;
import com.tinyengine.it.login.config.context.DefaultLoginUserContext;
import com.tinyengine.it.login.model.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class SSOInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String SSO_SERVER = "http://localhost:9090";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        log.info("Intercepting: {}, Token: {}", requestURI, token != null ? "present" : "null");

        // 如果没有token，重定向到登录页
        if (token == null || token.isEmpty()) {
            String redirectUrl = SSO_SERVER;
            log.info("No token, redirecting to: {}", redirectUrl);
            response.sendRedirect(redirectUrl);
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
            String tenantId = jwtUtil.getTenantIdFromToken(token);
            String roles = jwtUtil.getRolesFromToken(token);
            String renterId = jwtUtil.getRenterIdFromToken(token);
            Integer platformId = jwtUtil.getPlatformIdFromToken(token);
            String siteId = jwtUtil.getSiteIdFromToken(token);

            // 检查必需的用户信息
            if (username == null || username.isEmpty() || userId == null) {
                log.warn("User information is incomplete - username: {}, userId: {}", username, userId);
                response.sendRedirect(SSO_SERVER);
                return false;
            }

            // 存储用户信息到LoginUserContext
            UserInfo userInfo = new UserInfo(
                    userId, username, tenantId != null ? tenantId : "default-tenant"
            );
            userInfo.setRenterId(renterId != null ? renterId : "default-renter");
            userInfo.setPlatformId(platformId != null ? platformId : 0);
            userInfo.setSiteId(siteId != null ? siteId : "default-site");
            userInfo.setRoles(roles != null ? roles : "USER");
            userInfo.setToken(token);

            DefaultLoginUserContext.setCurrentUser(userInfo);

            log.info("Token validated and user context set for user: {}", username);
            log.info("User details - Tenant: {}, Platform: {}, Site: {}",
                    tenantId, platformId, siteId);
            return true;

        } catch (Exception e) {
            log.error("Token validation exception: {}", e.getMessage(), e);
            response.sendRedirect(SSO_SERVER + "/login");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        // 请求完成后清理用户上下文
        DefaultLoginUserContext.clear();
        log.debug("Cleared user context for request completion");
    }
}
