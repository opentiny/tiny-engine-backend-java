package com.tinyengine.it.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HashCalculator {
    public static String calculateMD5Hash(Map<String, Object> obj) {
        try {
            // 创建 MessageDigest 对象，并指定算法为 MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将对象转换为字节数组
            byte[] objBytes = obj.toString().getBytes();

            // 计算哈希值
            byte[] hashBytes = md.digest(objBytes);

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
