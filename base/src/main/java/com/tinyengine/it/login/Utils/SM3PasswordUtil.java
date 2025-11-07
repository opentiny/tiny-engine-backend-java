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

import com.tinyengine.it.login.model.PasswordResult;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Security;
import java.util.UUID;

/**
 * SM3 密码哈希工具类
 */
public class SM3PasswordUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String SM3_ALGORITHM = "SM3";
    private static final String PASSWORD_SALT_PREFIX = "SM3_";

    /**
     * SM3 哈希计算
     */
    public static String sm3Hash(String data, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance(SM3_ALGORITHM, "BC");
        String dataWithSalt = data + salt;
        byte[] hash = md.digest(dataWithSalt.getBytes("UTF-8"));
        return bytesToHex(hash);
    }

    /**
     * 创建用户密码
     */
    public static PasswordResult createPassword(String plainPassword) throws Exception {
        String salt = generateSalt();
        String passwordHash = sm3Hash(plainPassword, salt);
        return new PasswordResult(passwordHash, salt);
    }

    /**
     * 验证用户密码
     */
    public static boolean verifyPassword(String inputPassword, String storedHash, String salt) throws Exception {
        String computedHash = sm3Hash(inputPassword, salt);
        return computedHash.equals(storedHash);
    }

    /**
     * 生成随机盐值
     */
    private static String generateSalt() {
        return PASSWORD_SALT_PREFIX + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
