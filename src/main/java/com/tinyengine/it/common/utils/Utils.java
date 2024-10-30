package com.tinyengine.it.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Utils.
 *
 * @since 2024-10-20
 */
@Slf4j
public class Utils {
    /**
     * The Res keys.
     */
    private static final String[] RES_KEYS = {"is_body", "parent_id", "is_page", "is_default"};
    private static final Pattern CHAR_WORD = Pattern.compile("_(\\w)");
    private static final Pattern CHAR_AZ = Pattern.compile("([A-Z])");

    /**
     * Remove duplicates list.
     *
     * @param <T>  the type parameter
     * @param list the list
     * @return the list
     */
    // 泛型去重方法
    public static <T> List<T> removeDuplicates(List<T> list) {
        // 使用 Set 去重
        Set<T> set = new LinkedHashSet<>(list);
        // 返回去重后的 List
        return new ArrayList<>(set);
    }

    /**
     * Find max version string.
     *
     * @param versions the versions
     * @return the string
     */
    // 查找最大版本
    public static String findMaxVersion(List<String> versions) {
        return versions.stream()
                .max(Comparator.comparing(
                        version -> Arrays.stream(version.split("\\."))
                                .mapToInt(Integer::parseInt).toArray(),
                        Comparator.comparingInt((int[] arr) -> arr[0])
                                .thenComparingInt(arr -> arr[1])
                                .thenComparingInt(arr -> arr[2])))
                .orElse(null);
    }

    /**
     * To hump string.
     *
     * @param name the name
     * @return the string
     */
    public static String toHump(String name) {
        // 定义正则表达式模式
        Matcher matcher = CHAR_WORD.matcher(name);
        // 使用 StringBuilder 来构建结果字符串
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        // 遍历匹配的结果
        while (matcher.find()) {
            // 将之前的字符串添加到结果中
            result.append(name, lastEnd, matcher.start());

            // 获取匹配到的字母并转换为大写
            // 确保此处是有效的调用
            String match = matcher.group(1);
            result.append(match.toUpperCase(Locale.ROOT));

            lastEnd = matcher.end();
        }
        // 添加最后的部分
        result.append(name.substring(lastEnd));

        return result.toString();
    }

    /**
     * To line string.
     *
     * @param name the name
     * @return the string
     */
    public static String toLine(String name) {
        // 定义正则表达式模式
        Matcher matcher = CHAR_AZ.matcher(name);

        // 使用 StringBuilder 来构建结果字符串
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        // 遍历匹配的结果
        while (matcher.find()) {
            // 将之前的字符串添加到结果中
            result.append(name, lastEnd, matcher.start());

            // 在大写字母前添加下划线
            // 确保此处是有效的调用
            result.append("_").append(matcher.group(1));

            lastEnd = matcher.end();
        }
        // 添加最后的部分并转换为小写
        result.append(name.substring(lastEnd).toLowerCase(Locale.ROOT));

        return result.toString();
    }

    /**
     * Convert map.
     *
     * @param obj the obj
     * @return the map
     */
    // 对象转map
    public static Map<String, Object> convert(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 将对象转换为 JSON 字符串，然后再解析为 Map
        return objectMapper.convertValue(obj, Map.class);
    }

    private static boolean isSubset(String a, String b) {
        // 这里只做了简单的匹配，实际应用可能需要复杂的解析
        return a.equals(b) || a.endsWith("x") && b.startsWith(a.substring(0, a.length() - 1));
    }

    private static int compareMinVersion(String a, String b) {
        String minVersionA = getMinVersion(a);
        String minVersionB = getMinVersion(b);
        return compareVersions(minVersionA, minVersionB);
    }

    private static String getMinVersion(String version) {
        // 假设最小版本是版本号中的非 x 部分
        if (version.contains("x")) {
            return version.replaceAll("x", "0");
        }
        return version;
    }

    private static int compareVersions(String v1, String v2) {
        String[] v1Parts = v1.split("\\.");
        String[] v2Parts = v2.split("\\.");

        int length = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;

            if (v1Part < v2Part) {
                return -1;
            }
            if (v1Part > v2Part) {
                return 1;
            }
        }
        return 0;
    }
}
