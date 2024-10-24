package com.tinyengine.it.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Execute java script.
 *
 * @since 2024-10-20
 */
@Component
@Slf4j
public class ExecuteJavaScript {

    /**
     * Execute dsl java script list.
     *
     * @param command the command
     * @return the list
     */
    public List<Map<String, Object>> executeDslJavaScript(List<String> command) {

        StringBuilder result = ExecuteCommand.executeCommand(command);
        log.info("result" + result.toString());
        List<Map<String, Object>> resulList = new ArrayList<>();
        // 将 JSON 数组转换为 Java 数组
        try {
            resulList = JSON.parseObject(result.toString(), new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (JSONException e) {
            log.error("Error parsing JSON: " + e.getMessage());
        }
        return resulList;
    }

    /**
     * Execute trans form map.
     *
     * @param command the command
     * @return the map
     */
    public Map<String, Object> executeTransForm(List<String> command) {
        StringBuilder result = ExecuteCommand.executeCommand(command);
        log.info("result" + result.toString());
        Map<String, Object> resulMap = new HashMap<>();
        // 将 JSON 数组转换为 Java 数组
        try {
            resulMap = JSON.parseObject(result.toString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (JSONException e) {
            log.error("Error parsing JSON: " + e.getMessage());
        }
        return resulMap;
    }
}
