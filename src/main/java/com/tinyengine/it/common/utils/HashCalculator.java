package com.tinyengine.it.common.utils;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * The type Hash calculator.
 *
 * @since 2024-10-20
 */
@Slf4j
public class HashCalculator {
    /**
     * Calculate md 5 hash string.
     *
     * @param obj the obj
     * @return the string
     */
    public static String calculateMD5Hash(Map<String, Object> obj) {
        if (obj == null) {
            return "";
        }
        return SecureUtil.md5(obj.toString());
    }
}
