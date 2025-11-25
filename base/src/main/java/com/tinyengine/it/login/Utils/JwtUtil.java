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

package com.tinyengine.it.login.Utils;

import com.tinyengine.it.login.service.TokenBlacklistService;
import com.tinyengine.it.model.entity.Tenant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Jwt util
 */
@Component
@Slf4j
public class JwtUtil {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private static final long EXPIRATION_TIME = 21600000; // 6小时 = 6 * 60 * 60 * 1000 = 21600000 毫秒
    private static final String DEFAULT_SECRET = "tiny-engine-backend-secret-key-at-jwt-login";

    // 避免启动时环境变量未加载的问题
    private static String getSecretString() {
        return Optional.ofNullable(System.getenv("SECRET_STRING"))
            .orElse(DEFAULT_SECRET);
    }

    public static SecretKey getSecretKey() {

        return Keys.hmacShaKeyFor(getSecretString().getBytes());
    }

    /**
     * 生成包含完整用户信息的 JWT Token（支持 Tenant 对象和 Map 两种格式）
     */
    public String generateToken(String username, String roles, String userId,
        Object tenants, Integer platformId) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roles", roles);
        claims.put("userId", userId);
        claims.put("platformId", platformId);

        // 处理 tenants 参数，支持 List<Tenant> 和 List<Map>
        if (tenants instanceof List) {
            List<?> tenantList = (List<?>) tenants;
            if (!tenantList.isEmpty() && tenantList.get(0) instanceof Tenant) {
                // 如果是 Tenant 对象列表，转换为简化 Map
                claims.put("tenants", convertTenantsToSimpleMap((List<Tenant>) tenants));
            } else {
                // 如果是 Map 列表，直接使用
                claims.put("tenants", tenants);
            }
        } else {
            claims.put("tenants", new ArrayList<>());
        }

        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSecretKey())
            .compact();
    }

    /**
     * 重载方法，保持向后兼容
     */
    public String generateToken(String username, String roles, String userId,
        List<Tenant> tenants, Integer platformId) {
        return generateToken(username, roles, userId, (Object) tenants, platformId);
    }

    /**
     * 将 Tenant 对象列表转换为简化的 Map 列表（避免日期序列化问题）
     */
    private List<Map<String, Object>> convertTenantsToSimpleMap(List<Tenant> tenants) {
        if (tenants == null) {
            return new ArrayList<>();
        }

        return tenants.stream().map(tenant -> {
            Map<String, Object> simple = new HashMap<>();
            simple.put("id", tenant.getId());
            simple.put("orgCode", tenant.getOrgCode());
            simple.put("nameCn", tenant.getNameCn());
            simple.put("nameEn", tenant.getNameEn());
            simple.put("description", tenant.getDescription());
            simple.put("createdBy", tenant.getCreatedBy());
            simple.put("lastUpdatedBy", tenant.getLastUpdatedBy());
            // 不传递日期字段，避免序列化问题
            return simple;
        }).collect(Collectors.toList());
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to get username from token: " + e.getMessage());
            return null;
        }
    }

    /**
     * 从 Token 中获取角色信息
     */
    public String getRolesFromToken(String token) {

        return getClaimFromToken(token, "roles", String.class);
    }

    /**
     * 从 Token 中获取用户ID
     */
    public String getUserIdFromToken(String token) {

        return getClaimFromToken(token, "userId", String.class);
    }

    /**
     * 从 Token 中获取租户信息（返回 Map 列表）
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getTenantsFromToken(String token) {
        return getClaimFromToken(token, "tenants", List.class);
    }

    /**
     * 从 Token 中获取租户ID（保持向后兼容，返回 Tenant 对象列表）
     * 注意：这个方法会将 Map 转换为 Tenant 对象，但可能丢失一些字段
     */
    public List<Tenant> getTenantIdFromToken(String token) {
        List<Map<String, Object>> tenantMaps = getTenantsFromToken(token);
        if (tenantMaps == null) {
            return new ArrayList<>();
        }

        return tenantMaps.stream().map(map -> {
            Tenant tenant = new Tenant();
            tenant.setId((String) map.get("id"));
            tenant.setOrgCode((String) map.get("orgCode"));
            tenant.setNameCn((String) map.get("nameCn"));
            tenant.setNameEn((String) map.get("nameEn"));
            tenant.setDescription((String) map.get("description"));
            tenant.setCreatedBy((String) map.get("createdBy"));
            tenant.setLastUpdatedBy((String) map.get("lastUpdatedBy"));
            return tenant;
        }).collect(Collectors.toList());
    }

    /**
     * 从 Token 中获取平台ID
     */
    public Integer getPlatformIdFromToken(String token) {
        return getClaimFromToken(token, "platformId", Integer.class);
    }

    /**
     * 通用的claim获取方法
     */
    private <T> T getClaimFromToken(String token, String claimName, Class<T> clazz) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return claims.get(claimName, clazz);
        } catch (Exception e) {
            log.error("Failed to get claim '" + claimName + "' from token: " + e.getMessage());
            return null;
        }
    }

    /**
     * 验证 Token 是否有效（包含黑名单检查和过期检查）
     */
    public boolean validateToken(String token) {
        try {
            // 检查是否在黑名单中
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                log.warn("Token is blacklisted: {}", token);
                return false;
            }

            // 解析并验证 token
            Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            // 检查是否过期
            boolean isExpired = claims.getExpiration().before(new Date());
            if (isExpired) {
                log.warn("Token is expired: {}", token);
                return false;
            }

            return true;

        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Invalid token format: {}", e.getMessage());
            return false;
        } catch (SecurityException e) {
            log.warn("Token signature validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }


    /**
     * 选择特定租户并生成新 Token
     */
    public String generateTokenWithSelectedTenant(String token, List<Tenant> tenants) {
        return generateToken(
            getUsernameFromToken(token),
            getRolesFromToken(token),
            getUserIdFromToken(token),
            tenants,
            getPlatformIdFromToken(token)
        );
    }

    /**
     * 从请求中获取 Token
     */
    public String getTokenFromRequest(String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        }
        return tokenHeader;
    }
}
