package com.tinyengine.it.service.impl.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.I18nEntriesMapper;
import com.tinyengine.it.mapper.I18nLangsMapper;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.I18nEntries;
import com.tinyengine.it.model.entity.I18nLangs;
import com.tinyengine.it.service.app.I18nEntriesService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class I18nEntriesServiceImpl implements I18nEntriesService {

    @Autowired
    I18nEntriesMapper i18nEntriesMapper;
    @Autowired
    I18nLangsMapper i18nLangsMapper;

    private static final Logger logger = LoggerFactory.getLogger(I18nEntriesServiceImpl.class);

    /**
     * 查询表i18n_entries所有数据
     */
    @SystemServiceLog(description = "findAllI18nEntries 查询表i18n_entries所有数据")
    @Override
    public I18nEntriesListResult findAllI18nEntries() throws ServiceException {
        // 获取所属应用/区块的 语言列表  getHostLangs

        List<I18nLangs> i18nLangsList = getHostLangs();
        if (i18nLangsList == null || i18nLangsList.isEmpty()) {
            return null;
        }
        // 获取词条列表
        List<I18nEntriesDto> i18nEntriesList = new ArrayList<I18nEntriesDto>();
        i18nEntriesList = i18nEntriesMapper.findAllI18();
        if (i18nEntriesList == null) {
            return null;
        }
        // 格式化词条列表
        Map<String, Map<String, String>> messages = formatEntriesList(i18nEntriesList);
        List<I18nLangs> i18nLangsListTemp = i18nLangsList.stream().map(i18nLangs -> new I18nLangs(i18nLangs.getLang(), i18nLangs.getLabel())).collect(Collectors.toList());
        I18nEntriesListResult i18nEntriesListResult = new I18nEntriesListResult();
        i18nEntriesListResult.setI18nLangsList(i18nLangsListTemp);
        i18nEntriesListResult.setMessages(messages);
        return i18nEntriesListResult;
    }

    /**
     * 根据主键id查询表i18n_entries信息
     *
     * @param id
     */
    @Override
    public I18nEntries findI18nEntriesById(@Param("id") Integer id) throws ServiceException {
        return i18nEntriesMapper.findI18nEntriesById(id);
    }

    /**
     * 根据条件查询表i18n_entries数据
     *
     * @param i18nEntries
     */
    @Override
    public List<I18nEntries> findI18nEntriesByCondition(I18nEntries i18nEntries) throws ServiceException {
        return i18nEntriesMapper.findI18nEntriesByCondition(i18nEntries);
    }

    /**
     * 根据主键id删除表i18n_entries数据
     *
     * @param id
     */
    @Override
    public Integer deleteI18nEntriesById(@Param("id") Integer id) throws ServiceException {
        return i18nEntriesMapper.deleteI18nEntriesById(id);
    }

    /**
     * 根据主键id更新表i18n_entries数据
     *
     * @param i18nEntries
     */
    @Override
    public Integer updateI18nEntriesById(I18nEntries i18nEntries) throws ServiceException {
        return i18nEntriesMapper.updateI18nEntriesById(i18nEntries);
    }

    /**
     * 新增表i18n_entries数据
     *
     * @param i18nEntries
     */
    @Override
    public Integer createI18nEntries(I18nEntries i18nEntries) throws ServiceException {
        return i18nEntriesMapper.createI18nEntries(i18nEntries);
    }

    /**
     * 获取格式化词条列表
     *
     * @param i18nEntriesList
     * @return
     */
    @SystemServiceLog(description = "formatEntriesList 国际化词条实现类里获取格式化词条列表")
    @Override
    public Map<String, Map<String, String>> formatEntriesList(List<I18nEntriesDto> i18nEntriesList) {
        // 格式化词条列表
        Map<String, Map<String, String>> messages = new HashMap<>();
        for (I18nEntriesDto i18nEntries : i18nEntriesList) {
            String lang = i18nEntries.getLang().getLang();
            String key = i18nEntries.getKey();
            String content = i18nEntries.getContent();
            // 使用三元运算符检查是否存在 lang 对应的 map，如果不存在则创建
            messages.putIfAbsent(lang, new HashMap<>());
            // 现在可以将键值对存入 messages[lang] 对应的 map 中
            messages.get(lang).put(key, content);
        }
        return messages;
    }

    /**
     * 获取词条宿主支持的语言
     *
     * @return
     */
    @SystemServiceLog(description = "getHostLangs 获取词条宿主支持的语言")
    @Override
    public List<I18nLangs> getHostLangs() {
        // 先默认全部应用都支持中英文, 后续其他语言需要结合管理后台逻辑二次开发
        QueryWrapper<I18nLangs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lang", "zh_CN").or().eq("lang", "en_US");
        List<I18nLangs> i18nLangsList = i18nLangsMapper.selectList(queryWrapper);
        return i18nLangsList;
    }

    /**
     * 创建多词条国际化
     *
     * @param operateI18nEntries
     * @return
     */
    @Override
    public List<I18nEntries> Create(OperateI18nEntries operateI18nEntries) {
        List<I18nLangs> langs = getHostLangs();
        Map<String, Integer> langsDic = new HashMap<>();
        for (I18nLangs i18nLangs : langs) {
            langsDic.put(i18nLangs.getLang(), i18nLangs.getId());
        }

        List<I18nEntries> i18nEntriesList = fillParam(operateI18nEntries, langsDic);
        i18nEntriesList.stream().map(entry -> createI18nEntries(entry)).collect(Collectors.toList());
        return i18nEntriesList;
    }


    /**
     * 获取该词条下多语言的参数
     *
     * @param operateI18NEntries
     * @return
     */
    @SystemServiceLog(description = "getEntriesParam 获取该词条下多语言的参数")
    public List<I18nEntries> getEntriesParam(OperateI18nBatchEntries operateI18NEntries) {
        List<I18nLangs> i18nLangsList = getHostLangs();
        if (i18nLangsList == null) {
            return null;
        }
        return formatEntriesParam(operateI18NEntries, i18nLangsList);
    }

    /**
     * 格式化词条参数
     *
     * @param operateI18nBatchEntries
     * @param i18nLangsList
     * @return
     */
    @SystemServiceLog(description = "formatEntriesParam 格式化词条参数")
    public List<I18nEntries> formatEntriesParam(OperateI18nBatchEntries operateI18nBatchEntries, List<I18nLangs> i18nLangsList) {
        Map<String, Integer> langsDic = new HashMap<>();
        List<I18nEntries> i18nEntriesList = new ArrayList<>();
        List<I18nEntries> i18nEntriesListResult = new ArrayList<>();
        for (I18nLangs i18nLangs : i18nLangsList) {
            langsDic.put(i18nLangs.getLang(), i18nLangs.getId());
        }
        // 需要两种类型判断  待定 操作批量国际化
        for (Entry entry : operateI18nBatchEntries.getEntries()) {
            OperateI18nEntries operateI18nEntries = new OperateI18nEntries();
            operateI18nEntries.setHost(operateI18nBatchEntries.getHost());
            operateI18nEntries.setHost_type(operateI18nBatchEntries.getHost_type());
            operateI18nEntries.setKey(entry.getKey());
            operateI18nEntries.setContents(entry.getContents());
            i18nEntriesList = fillParam(operateI18nEntries, langsDic);
            i18nEntriesListResult.addAll(i18nEntriesList);
        }

        return i18nEntriesListResult;
    }

    /**
     * 填充参数
     *
     * @param operateI18nEntries
     * @param langsDic
     * @return
     */
    @SystemServiceLog(description = "fillParam 填充参数")
    public List<I18nEntries> fillParam(OperateI18nEntries operateI18nEntries, Map<String, Integer> langsDic) {
        List<I18nEntries> i18nEntriesList = new ArrayList<>();

        Map<String, String> contents = operateI18nEntries.getContents();

        contents.keySet().forEach(item -> {
            int lang = langsDic.get(item);
            if (lang != 0) {
                I18nEntries i18nEntries = new I18nEntries();
                i18nEntries.setHost(Integer.valueOf(operateI18nEntries.getHost()));
                i18nEntries.setKey(operateI18nEntries.getKey());
                i18nEntries.setLangId(Integer.valueOf(lang));
                i18nEntries.setHostType(operateI18nEntries.getHost_type());
                i18nEntries.setContent(contents.get(item));
                i18nEntriesList.add(i18nEntries);
            }
        });
        return i18nEntriesList;
    }

    /**
     * 批量创建
     *
     * @param operateI18nBatchEntries
     * @return
     */
    @SystemServiceLog(description = "bulkCreate 国际化词条批量创建")
    @Override
    public List<I18nEntries> bulkCreate(OperateI18nBatchEntries operateI18nBatchEntries) {
        // getEntriesParam
        List<I18nEntries> i18nEntriesList = getEntriesParam(operateI18nBatchEntries);

        // bulkCreateEntries
        i18nEntriesList.stream().map(entry -> createI18nEntries(entry)).collect(Collectors.toList());
        return i18nEntriesList;

    }

    /**
     * 批量更新
     *
     * @param operateI18nEntries
     * @return
     */
    @SystemServiceLog(description = "bulkUpdateEntries 国际化词条批量更新")
    public List<I18nEntries> bulkUpdateEntries(OperateI18nEntries operateI18nEntries) {
        List<I18nLangs> langs = getHostLangs();
        Map<String, Integer> langsDic = new HashMap<>();
        for (I18nLangs i18nLangs : langs) {
            langsDic.put(i18nLangs.getLang(), i18nLangs.getId());
        }

        List<I18nEntries> i18nEntriesList = fillParam(operateI18nEntries, langsDic);
        // bulkCreateEntries
        for (I18nEntries i18Entries : i18nEntriesList) {

            i18nEntriesMapper.updateByEntries(i18Entries.getContent(), i18Entries.getHost(), i18Entries.getHostType(), i18Entries.getKey(), i18Entries.getLangId());
        }
        return i18nEntriesList;
    }

    /**
     * 批量更新
     *
     * @param operateI18nEntries
     * @return
     */
    @SystemServiceLog(description = "bulkUpdate 批量更新")
    @Override
    public List<I18nEntries> bulkUpdate(OperateI18nEntries operateI18nEntries) {
        List<I18nEntries> i18nEntriesList = new ArrayList<>();
        if (operateI18nEntries.getHost_type().equals("app")) {
            I18nEntries i18nEntries = new I18nEntries();
            i18nEntries.setHostType(operateI18nEntries.getHost_type());
            i18nEntries.setHost(Integer.valueOf(operateI18nEntries.getHost()));
            i18nEntries.setKey(operateI18nEntries.getKey());
            i18nEntriesList = i18nEntriesMapper.findI18nEntriesByCondition(i18nEntries);
            if (i18nEntriesList.isEmpty()) {
                i18nEntriesList = Create(operateI18nEntries);
                return i18nEntriesList;
            }
        }
        i18nEntriesList = bulkUpdateEntries(operateI18nEntries);
        return i18nEntriesList;
    }

    /**
     * @param host
     * @param hostType
     * @param keys
     * @return
     */
    @SystemServiceLog(description = "deleteI18nEntriesByHostAndHostTypeAndKey 根据host、host_type、key查询删除国际化词条")
    @Override
    public List<I18nEntries> deleteI18nEntriesByHostAndHostTypeAndKey(String host, String hostType, List<String> keys) {
        List<I18nEntries> i18nEntriesList = new ArrayList<>();

        for (String key : keys) {
            I18nEntries i18nEntries = new I18nEntries();
            i18nEntries.setHostType(hostType);
            i18nEntries.setHost(Integer.valueOf(host));
            i18nEntries.setKey(key);
            i18nEntriesList = i18nEntriesMapper.findI18nEntriesByCondition(i18nEntries);
            i18nEntriesList.stream().forEach(i18nEntriesDto -> deleteI18nEntriesById(i18nEntriesDto.getId()));
        }
        return i18nEntriesList;
    }


    /**
     * 上传单个文件
     *
     * @param file
     * @param host
     * @throws Exception
     */
    @SystemServiceLog(description = "readSingleFileAndBulkCreate 上传单个国际化文件")
    @Override
    public Result<Map<String, Object>> readSingleFileAndBulkCreate(String lang, MultipartFile file, int host) throws Exception {

        List<Object> entriesArr = new ArrayList<>();
        Map<String, Object> entriesItem = new HashMap<>();
        String contentType = file.getContentType();

        if (contentType.equals(Enums.E_MimeType.Json.getValue())) {
            Result<Map<String, Object>> parseJsonFileStreamResult = parseJsonFileStream(lang, file);
            if (!parseJsonFileStreamResult.isSuccess()) {
                return parseJsonFileStreamResult;
            }
            entriesItem = parseJsonFileStreamResult.getData();
            entriesArr.add(entriesItem);
        } else {
            entriesItem = parseZipFileStream(lang, file);
            entriesArr.add(entriesItem);
        }
        // 批量上传接口未提交任何文件流时报错
        if (entriesArr.isEmpty()) {
            throw new Exception("No file uploaded");
        }

        Map<String, Object> bulkCreateOrUpdateNumReturn = bulkCreateOrUpdate(entriesArr, host);
        return Result.success(bulkCreateOrUpdateNumReturn);

    }

    /**
     * 批量上传词条数据
     *
     * @param file
     * @param host
     */
    @SystemServiceLog(description = "readFilesAndbulkCreate 批量上传词条数据")
    @Override
    public Result<Map<String, Object>> readFilesAndbulkCreate(String lang, MultipartFile file, int host) throws Exception {

        List<Object> entriesArr = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        Result<Map<String, Object>> parseJsonFileStreamResult = parseJsonFileStream(lang, file);
        // 解析 JSON 数据
        if (!parseJsonFileStreamResult.isSuccess()) {
            return parseJsonFileStreamResult;
        }
        Map<String, Object> entriesItem = parseJsonFileStreamResult.getData();
        entriesArr.add(entriesItem);
        // 批量上传接口未提交任何文件流时报错
        if (entriesArr.isEmpty()) {
            throw new Exception("No file uploaded");
        }

        Map<String, Object> bulkCreateOrUpdateNumReturn = bulkCreateOrUpdate(entriesArr, host);
        return Result.success(bulkCreateOrUpdateNumReturn);

    }


    /**
     * 批量创建或修改
     *
     * @param entriesArr
     * @param host
     * @return
     */
    @SystemServiceLog(description = "bulkCreateOrUpdate 批量创建或修改")
    public Map<String, Object> bulkCreateOrUpdate(List<Object> entriesArr, int host) {
        List<I18nEntries> entries = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        entriesArr.forEach(langEntry -> {
            try {
                if (langEntry instanceof Map) {
                    resultMap.putAll((Map<? extends String, ?>) langEntry);
                }

                int lang = (int) resultMap.get("lang");
                Map<String, Object> langEntries = (Map<String, Object>) resultMap.get("entries");
                langEntries.forEach((key, value) -> {
                    I18nEntries i18nEntries = new I18nEntries();
                    i18nEntries.setKey(key);
                    i18nEntries.setLangId(lang);
                    i18nEntries.setHost(host);
                    i18nEntries.setHostType("app");
                    i18nEntries.setContent((String) value);
                    entries.add(i18nEntries);
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        // 超大量数据更新，如上传国际化文件，不返回插入或更新的词条
        Map<String, Object> bulkInsertOrUpdateNumReturn = bulkInsertOrUpdate(entries);
        return bulkInsertOrUpdateNumReturn;

    }

    /**
     * 超大量数据更新，如上传国际化文件，不返回插入或更新的词条
     *
     * @param entries
     */
    @SystemServiceLog(description = "bulkInsertOrUpdate 超大量数据更新")
    public Map<String, Object> bulkInsertOrUpdate(List<I18nEntries> entries) {
        int addNum = 0;
        int updateNum = 0;
        for (I18nEntries entry : entries) {
            // 构建查询条件，假设 key 作为唯一键
            // 查询数据库中是否存在该记录
            I18nEntries existingEntry = i18nEntriesMapper.findI18nEntriesByKeyAndLang(entry.getKey(), entry.getLangId());

            if (existingEntry == null) {
                // 插入新记录
                createI18nEntries(entry);
                addNum = addNum + 1;
            } else {
                // 更新记录
                I18nEntries i18nEntries = new I18nEntries();
                i18nEntries.setContent(entry.getContent());
                i18nEntries.setId(existingEntry.getId());
                updateI18nEntriesById(i18nEntries);
                updateNum = updateNum + 1;
            }
        }

        // 构造返回插入和更新的条数
        Map<String, Object> mapNumResult = new HashMap<>();
        mapNumResult.put("insertNum", addNum);
        mapNumResult.put("updateNum", updateNum);
        return mapNumResult;
    }

    /**
     * 解析JSON文件
     *
     * @param file
     * @return
     */
    public Result<Map<String, Object>> parseJsonFileStream(String lang, MultipartFile file) throws Exception {
        String encoding = StandardCharsets.UTF_8.name(); // 默认使用UTF-8
        String fieldname = lang; // fieldname 为i18n_langs的id
        String filename = file.getOriginalFilename();
        logger.info("parseJsonFileStream field: " + fieldname + ", filename:" + filename + ", encoding:" + encoding + ", mime:" + file.getContentType());

        // 校验文件流合法性
        validateFileStream(file, ExceptionEnum.CM308.getResultCode(), Arrays.asList(Enums.E_MimeType.Json.getValue()));

        // 解析国际化词条文件
        Map<String, Object> entriesItem = new HashMap<>();
        entriesItem.put("lang", Integer.parseInt(fieldname));
        entriesItem.put("entries", new HashMap<String, Object>());


        // 读取上传的 JSON 文件内容
        String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        String jsonFileName = UUID.randomUUID().toString() + "_" + filename.toLowerCase();

        Path target = Paths.get("/path/to/tmp", jsonFileName);
        try (FileOutputStream fos = new FileOutputStream(String.valueOf(target))) {
            fos.write(jsonContent.getBytes());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> jsonData = objectMapper.readValue(jsonContent, new TypeReference<Map<String, Object>>() {
            });
            entriesItem.put("entries", flat(jsonData));
        } catch (IOException e) {
            return Result.validateFailed("parse Json error");
        } finally {
            Files.deleteIfExists(target);
            file.getInputStream().close(); // Close file stream
        }

        return Result.success(entriesItem);
    }

    /**
     * 将一个嵌套的 JSON 对象扁平化
     *
     * @param jsonData
     * @return
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

    /**
     * 校验文件流合法性
     *
     * @param file      文件
     * @param code      报错码
     * @param mimeTypes 文件类型集合
     * @throws Exception
     */
    public void validateFileStream(MultipartFile file, String code, List<String> mimeTypes) throws Exception {
        boolean condition = file.getOriginalFilename() != null && file.getName().matches("\\d+")
                && mimeTypes.contains(file.getContentType());
        if (condition) {
            return;
        }

        // 只要文件不合法就throw error， 无论是批量还是单个
        file.getInputStream().close();
        throw new Exception(code);

    }


    /**
     * 解析zip文件
     *
     * @param file
     * @return
     */
    public Map<String, Object> parseZipFileStream(String lang, MultipartFile file) throws Exception {

        String encoding = StandardCharsets.UTF_8.name(); // 默认使用UTF-8
        String fieldname = lang;
        String filename = file.getOriginalFilename();
        logger.info("parseZipFileStream field: " + fieldname + ", filename:" + filename + ", encoding:" + encoding + ", mime:" + file.getContentType());

        // 校验文件流合法性
        validateFileStream(file, ExceptionEnum.CM314.getResultCode(), Arrays.asList(Enums.E_MimeType.Zip.getValue(), Enums.E_MimeType.xZip.getValue()));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> entriesItem = new HashMap<>();
        entriesItem.put("lang", Integer.parseInt(fieldname));
        entriesItem.put("entries", new HashMap<String, Object>());

        // 解压zip文件
        String target = null;
        try {

            // 将上传的文件保存到临时文件中
            File tempFile = File.createTempFile("/path/to/tmp", ".zip");
            target = tempFile.getAbsolutePath();
            file.transferTo(tempFile);

            // 解压ZIP文件并处理
            String content = extractAndProcessZipFile(tempFile);
            // 对返回的内容字符串进行组装
            Map<String, Object> jsonData = new LinkedHashMap<>();

            // 分割字符串并处理每个JSON对象
            String[] jsonObjects = content.split("\\}\\s*\\{");
            for (String json : jsonObjects) {
                // 加上缺失的花括号
                int num = checkMissingBrace(json);
                if (num == 1) {
                    json = json + "}";
                } else if (num == 2) {
                    json = "{" + json;
                }

                // 将JSON字符串转换为Map对象
                Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
                });
                // 把转换后的map铺平合并到总的Map中
                jsonData.putAll(flat(map));
            }

            entriesItem.put("entries", jsonData);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // 清理临时文件
            cleanUp(target);
        }

        return entriesItem;
    }


    /**
     * 删除临时目录以及内容
     *
     * @param targetDir
     * @throws IOException
     */
    private void cleanUp(String targetDir) throws IOException {
        Path directory = Paths.get(targetDir);
        Files.walk(directory)
                .sorted((o1, o2) -> -o1.compareTo(o2)) // reverse order to delete deepest files first
                .map(Path::toFile)
                .forEach(File::delete);
    }

    /**
     * 解压并处理zip文件，把读取到的JSON文件内容以字符串返回
     *
     * @param zipFile
     * @return
     * @throws IOException
     */
    private String extractAndProcessZipFile(File zipFile) throws IOException {

        StringBuilder jsonResult = new StringBuilder();
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.endsWith(".json")) {

                    // 处理JSON文件
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {

                        jsonResult.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
                    }
                    jsonResult.append("\n");

                }
            }
        }

        return jsonResult.toString();
    }


    /**
     * 检查对于读取到的JSON文件中的字符串缺失的是前花括号还是后花括号
     * (1: 缺失后花括号, 2: 缺失前花括号, 3:不缺)
     *
     * @param jsonString
     */
    public static int checkMissingBrace(String jsonString) {
        int openBraceCount = 0;
        int closeBraceCount = 0;

        for (char c : jsonString.toCharArray()) {
            if (c == '{') {
                openBraceCount++;
            } else if (c == '}') {
                closeBraceCount++;
            }
        }

        if (openBraceCount > closeBraceCount) {
            return 1;
        } else if (closeBraceCount > openBraceCount) {
            return 2;
        } else {
            return 3;
        }
    }


}
