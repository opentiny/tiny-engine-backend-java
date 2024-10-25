package com.tinyengine.it.common.utils;

import com.tinyengine.it.model.dto.SchemaConfig;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type Schema.
 *
 * @since 2024-10-20
 */
@Slf4j
public class Schema {
    // 使用一个 Set 来检查是否包含特定的字符串
    private static final Set<String> GROUPS = new HashSet<>();

    static {
        GROUPS.add("static");
        GROUPS.add("public");
    }

    /**
     * Assemble fields map.
     *
     * @param appData the app data
     * @param type    the type
     * @return the map
     */
    public Map<String, Object> assembleFields(Map<String, Object> appData, String type) {
        SchemaConfig conf = new SchemaConfig();

        // 根据类型选择对应的配置方法
        switch (type) {
            case "app":
                appData = processFields(appData, conf.getAppInclude(), conf.getAppFormat(), conf.getAppConvert());
                break;
            case "pageMeta":
                appData = processFields(appData, conf.getPageMetaInclude(), conf.getPageMetaFormat(),
                    conf.getPageMetaConvert());
                break;
            case "pageContent":
                if (!conf.getPageContentInclude().isEmpty()) {
                    appData = this.filterFields(appData, conf.getPageContentInclude());
                }
                break;
            default:
                appData =
                    processFields(appData, conf.getFolderInclude(), conf.getFolderFormat(), conf.getFolderConvert());
                break;
        }

        return appData;
    }

    // 提取公共处理逻辑
    private Map<String, Object> processFields(Map<String, Object> appData, List<String> includeConfig,
        Map<String, String> formatConfig, Map<String, String> convertConfig) {
        if (!includeConfig.isEmpty()) {
            appData = this.filterFields(appData, includeConfig);
        }
        if (!formatConfig.isEmpty()) {
            appData = this.formatFields(appData, formatConfig);
        }
        if (!convertConfig.isEmpty()) {
            appData = this.convertFields(appData, convertConfig);
        }
        return appData;
    }

    /**
     * Gets folder schema.
     *
     * @param data the data
     * @return the folder schema
     */
    public Map<String, Object> getFolderSchema(Map<String, Object> data) {
        String type = "folder";
        Map<String, Object> schema = assembleFields(data, type);
        schema.put("componentName", "Folder");

        return schema;
    }

    /**
     * Gets schema base.
     *
     * @param data the data
     * @return the schema base
     */
    public Map<String, Object> getSchemaBase(Map<String, Object> data) {
        Map<String, Object> pageContent = (Map<String, Object>)data.get("page_content");
        pageContent.put("fileName", data.get("name"));
        String type = "pageContent";
        return assembleFields(pageContent, type);
    }

    /**
     * Filter fields map.
     *
     * @param data        the data
     * @param includeConf the include conf
     * @return the map
     */
    protected Map<String, Object> filterFields(Map<String, Object> data, List<String> includeConf) {
        Map<String, Object> res = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (includeConf.contains(entry.getKey())) {
                res.put(entry.getKey(), entry.getValue());
            }
        }
        return res;
    }

    /**
     * Format fields map.
     *
     * @param data         the data
     * @param formatConfig the format config
     * @return the map
     */
    // 格式化字段
    protected Map<String, Object> formatFields(Map<String, Object> data, Map<String, String> formatConfig) {
        Map<String, Object> formattedData = new HashMap<>(data);
        for (Map.Entry<String, String> entry : formatConfig.entrySet()) {
            String key = entry.getKey();
            String funcName = entry.getValue();
            try {
                // 使用反射调用相应的格式化方法
                java.lang.reflect.Method method = this.getClass().getMethod(funcName, Object.class);
                Object value = data.get(key);
                formattedData.put(key, method.invoke(this, value));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return formattedData;
    }

    /**
     * Convert fields map.
     *
     * @param data        the data
     * @param convertConf the convert conf
     * @return the map
     */
    protected Map<String, Object> convertFields(Map<String, Object> data, Map<String, String> convertConf) {

        Set<Map.Entry<String, String>> entries = convertConf.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String oldKey = entry.getKey();
            String newKey = entry.getValue();

            if (data.containsKey(oldKey)) {
                data.put(newKey, data.get(oldKey));
                data.remove(oldKey);
            }
        }
        return data;
    }

    /**
     * To format string string.
     *
     * @param value the value
     * @return the string
     */
    // 定义格式化方法
    public String toFormatString(Object value) {
        return value.toString();
    }

    /**
     * To local timestamp string.
     *
     * @param value the value
     * @return the string
     */
    // 工具转换函数
    // utc时间转换为本地时间
    public String toLocalTimestamp(Object value) {
        try {
            // 使用适当的日期格式
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = isoFormat.parse(value.toString());
            SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return localFormat.format(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return value.toString();
        }
    }

    /**
     * To creator name string.
     *
     * @param value the value
     * @return the string
     */
    // createdBy 转换为用户名
    public String toCreatorName(Object value) {
        if (value == null) {
            return "Creator: ";
        }
        return "Creator: " + value.toString();
    }

    /**
     * To array value object [ ].
     *
     * @param value the value
     * @return the object [ ]
     */
    // 给global_state设置默认值
    public Object[] toArrayValue(Object value) {
        if (value instanceof Object[]) {
            return (Object[])value;
        }
        return new Object[] {value};
    }

    /**
     * To root element string.
     *
     * @param isBody the is body
     * @return the string
     */
    // isBody属性转换
    public String toRootElement(Object isBody) {
        if (isBody instanceof Boolean) {
            // 处理布尔值的格式化
            return ((Boolean)isBody) ? "body" : "div";
        }
        return "";
    }

    /**
     * To group name string.
     *
     * @param group the group
     * @return the string
     */
    // group名称转换
    public String toGroupName(String group) {
        // 调整一下group命名
        if (GROUPS.contains(group)) {
            return group + "Pages";
        }
        return group;
    }
}
