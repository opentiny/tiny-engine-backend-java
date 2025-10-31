package com.tinyengine.it.login.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 3600000; // 1小时
    private static final String SECRET_STRING = "your-secret-key-at-least-32-chars-long-here";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    /**
     * 生成包含完整用户信息的 JWT Token
     */
    public String generateToken(String username, String roles, String userId,
        String tenantId, String renterId, Integer platformId, String siteId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roles", roles);
        claims.put("userId", userId);
        claims.put("tenantId", tenantId);
        claims.put("renterId", renterId);
        claims.put("platformId", platformId);
        claims.put("siteId", siteId);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("Failed to get username from token: " + e.getMessage());
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
     * 从 Token 中获取租户ID
     */
    public String getTenantIdFromToken(String token) {
        return getClaimFromToken(token, "tenantId", String.class);
    }

    /**
     * 从 Token 中获取业务租户ID
     */
    public String getRenterIdFromToken(String token) {
        return getClaimFromToken(token, "renterId", String.class);
    }

    /**
     * 从 Token 中获取平台ID
     */
    public Integer getPlatformIdFromToken(String token) {
        return getClaimFromToken(token, "platformId", Integer.class);
    }

    /**
     * 从 Token 中获取站点ID
     */
    public String getSiteIdFromToken(String token) {
        return getClaimFromToken(token, "siteId", String.class);
    }

    /**
     * 通用的claim获取方法
     */
    private <T> T getClaimFromToken(String token, String claimName, Class<T> clazz) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get(claimName, clazz);
        } catch (Exception e) {
            System.err.println("Failed to get claim '" + claimName + "' from token: " + e.getMessage());
            return null;
        }
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
