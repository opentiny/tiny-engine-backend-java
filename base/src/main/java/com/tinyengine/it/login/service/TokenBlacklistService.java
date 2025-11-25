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

package com.tinyengine.it.login.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final Map<String, Long> tokenExpiryMap = new ConcurrentHashMap<>();

    /**
     * 将 token 加入黑名单
     */
    public void blacklistToken(String token, long expiryTime) {
        blacklistedTokens.add(token);
        tokenExpiryMap.put(token, expiryTime);

        // 定期清理过期的黑名单 token
        cleanExpiredTokens();
    }

    /**
     * 检查 token 是否在黑名单中
     */
    public boolean isTokenBlacklisted(String token) {
        cleanExpiredTokens();
        return blacklistedTokens.contains(token);
    }

    /**
     * 清理过期的黑名单 token
     */
    private void cleanExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        tokenExpiryMap.entrySet().removeIf(entry -> {
            if (entry.getValue() < currentTime) {
                blacklistedTokens.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}

