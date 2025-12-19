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

package com.tinyengine.it.common.utils;

import org.springframework.web.multipart.MultipartFile;
import java.security.MessageDigest;
import java.io.InputStream;

/**
 * The type MultipartFileHashUtils.
 *
 * @since 2025-12-17
 */
public class MultipartFileHashUtils {

    /**
     * 计算MultipartFile的MD5哈希
     */
    public static String getMultipartFileMD5(MultipartFile file) throws Exception {
        return getMultipartFileHash(file, "MD5");
    }

    /**
     * 计算MultipartFile的SHA-256哈希
     */
    public static String getMultipartFileSHA256(MultipartFile file) throws Exception {
        return getMultipartFileHash(file, "SHA-256");
    }

    /**
     * 通用方法：计算MultipartFile的哈希值
     */
    public static String getMultipartFileHash(MultipartFile file, String algorithm)
        throws Exception {

        MessageDigest digest = MessageDigest.getInstance(algorithm);

        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        return bytesToHex(digest.digest());
    }

    /**
     * 字节数组转十六进制字符串
     */
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
