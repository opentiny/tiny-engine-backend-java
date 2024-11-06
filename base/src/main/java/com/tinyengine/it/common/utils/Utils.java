package com.tinyengine.it.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tinyengine.it.model.dto.FileInfo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    /**
     * 解压并处理zip文件，把读取到的JSON文件内容以字符串返回
     *
     * @param multipartFile multipartFile
     * @return String
     * @throws IOException IOException
     */
    public static List<FileInfo> unzip(MultipartFile multipartFile) throws IOException {
        List<FileInfo> fileInfoList = new ArrayList<>();
        File tempDir = Files.createTempDirectory("unzip").toFile(); // 创建临时目录

        // 将 MultipartFile 转换为 File
        File zipFile = convertMultipartFileToFile(multipartFile);

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File newFile = new File(tempDir, zipEntry.getName());
                processZipFile(zipEntry, fileInfoList, newFile, zis);
                zis.closeEntry();
            }
        } finally {
            // 清理临时目录和文件
            for (File file : tempDir.listFiles()) {
                file.delete();
            }
            tempDir.delete();
            zipFile.delete(); // 删除临时的 zip 文件
        }
        return fileInfoList;
    }

    private static void processZipFile(ZipEntry zipEntry, List<FileInfo> fileInfoList, File newFile,
                                       ZipInputStream zis) throws IOException {
        if (zipEntry.isDirectory()) {
            fileInfoList.add(new FileInfo(newFile.getName(), "", true));
        } else {
            Files.createDirectories(newFile.getParentFile().toPath()); // 确保父目录存在
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile))) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zis.read(buffer)) != -1) {
                    bos.write(buffer, 0, length);
                }
            }
            fileInfoList.add(new FileInfo(newFile.getName(), readFileContent(newFile), false));
        }
    }

    // 转换 MultipartFile 为 File 的方法
    private static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        tempFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    public static String readFileContent(File file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        }
        return contentBuilder.toString();
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将一个嵌套的 JSON 对象扁平化
     *
     * @param jsonData the json data
     * @return map
     */
    public static Map<String, Object> flat(Map<String, Object> jsonData) {
        Map<String, Object> flattenedMap = new HashMap<>();
        flatten("", jsonData, flattenedMap);
        return flattenedMap;
    }

    private static void flatten(String prefix, Map<String, Object> data, Map<String, Object> flattenedMap) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            if (entry.getValue() instanceof Map) {
                flatten(key, (Map<String, Object>) entry.getValue(), flattenedMap);
            } else {
                flattenedMap.put(key, entry.getValue());
            }
        }
    }
}
