package com.tinyengine.it.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import cn.hutool.crypto.SecureUtil;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculator Test
 *
 * @since 2024-10-26
 */
class HashCalculatorTest {

    @Test
    void calculateMD5Hash() {
        Map<String, Object> data = new HashMap<>();
        data.put("test", "dfasdfasd");
        data.put("number", 1);
        String expected = "9a7e6fc5453dd9f6e530c58dce2598cd";
        assertEquals(expected, SecureUtil.md5(data.toString()));
    }
}