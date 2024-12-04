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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class UtilsTest {
    @Test
    void removeDuplicates() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("a");
        list.add("c");
        assertThat(list.size()).isEqualTo(4);
        List<String> result = Utils.removeDuplicates(list);
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void testFindMaxVersion() {
        String result = Utils.findMaxVersion(Arrays.<String>asList("versions"));
        assertEquals("versions", result);
    }

    @Test
    void testToHump() {
        String result = Utils.toHump("name");
        assertEquals("name", result);
    }

    @Test
    void testToLine() {
        String result = Utils.toLine("name");
        assertEquals("name", result);
    }

    @Test
    void testConvert() {
        Map<String, Object> mapData = new HashMap();
        mapData.put("key", "value");
        Map<String, Object> result = Utils.convert(mapData);
        assertEquals("value", result.get("key"));
    }

    @Test
    void testFlat() {
        Map<String, Object> mapData = new HashMap();
        mapData.put("key", "value");
        Map<String, Object> flat = Utils.flat(mapData);
        assertTrue(flat.keySet().contains("key"));
    }

    @Test
    void testReadFileContent() {
        URL resource = UtilsTest.class.getClassLoader().getResource("testFile.txt");
        if (resource != null) {
            File file = new File(resource.getFile());
            String fileContent = Utils.readFileContent(file);
            assertEquals("abc" + System.lineSeparator(), fileContent);
        }
    }
}

