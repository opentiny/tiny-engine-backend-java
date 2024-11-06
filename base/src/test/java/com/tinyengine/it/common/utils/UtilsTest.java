package com.tinyengine.it.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals("versions", result);
    }

    @Test
    void testToHump() {
        String result = Utils.toHump("name");
        Assertions.assertEquals("name", result);
    }

    @Test
    void testToLine() {
        String result = Utils.toLine("name");
        Assertions.assertEquals("name", result);
    }

    @Test
    void testConvert() {
        Map<String, Object> mapData = new HashMap();
        mapData.put("key", "value");
        Map<String, Object> result = Utils.convert(mapData);
        Assertions.assertEquals("value", result.get("key"));
    }
}

