package com.tinyengine.it.login.model;

import lombok.Data;

/**
 * 密码创建结果
 */
@Data
public class PasswordResult {
    private final String passwordHash;
    private final String salt;

    public PasswordResult(String passwordHash, String salt) {
        this.passwordHash = passwordHash;
        this.salt = salt;
    }
}
