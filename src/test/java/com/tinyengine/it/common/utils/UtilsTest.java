
package com.tinyengine.it.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
}