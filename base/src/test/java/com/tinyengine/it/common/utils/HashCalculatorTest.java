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

import cn.hutool.crypto.SecureUtil;

import org.junit.jupiter.api.Assertions;
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
        Assertions.assertEquals(expected, SecureUtil.md5(data.toString()));
        Assertions.assertEquals(expected, HashCalculator.calculateMD5Hash(data));
        Assertions.assertEquals("", HashCalculator.calculateMD5Hash(null));
    }
}