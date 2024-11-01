package com.tinyengine.it.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.utils.Utils;
import com.tinyengine.it.config.log.SystemServiceLog;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.I18nLangMapper;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.model.entity.I18nLang;
import com.tinyengine.it.service.app.I18nEntryService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The type 18 n entry service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class I18nEntryServiceImpl implements I18nEntryService {
    private static final Logger logger = LoggerFactory.getLogger(I18nEntryServiceImpl.class);
    @Autowired
    private I18nEntryMapper i18nEntryMapper;
    @Autowired
    private I18nLangMapper i18nLangMapper;

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

    /**
     * 检查对于读取到的JSON文件中的字符串缺失的是前花括号还是后花括号 (1: 缺失后花括号, 2: 缺失前花括号, 3:不缺)
     *
     * @param jsonString the json string
     * @return the int
     */
    public static int checkMissingBrace(String jsonString) {
        int openBraceCount = 0;
        int closeBraceCount = 0;

        for (char c : jsonString.toCharArray()) {
            if (c == '{') {
                openBraceCount++;
            }
            if (c == '}') {
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

    /**
     * 查询表t_i18n_entry所有数据
     *
     * @return I18nEntryListResult
     */
    @Override
    public I18nEntryListResult findAllI18nEntry() {
        I18nEntryListResult i18nEntriesListResult = new I18nEntryListResult();
        // 获取所属应用/区块的 语言列表 getHostLangs
        List<I18nLang> i18nLangsList = getHostLangs();
        if (i18nLangsList == null || i18nLangsList.isEmpty()) {
            return i18nEntriesListResult;
        }
        // 获取词条列表
        List<I18nEntryDto> i18nEntriesList = i18nEntryMapper.queryAllI18nEntry();
        if (i18nEntriesList == null) {
            return i18nEntriesListResult;
        }
        // 格式化词条列表
        SchemaI18n messages = formatEntriesList(i18nEntriesList);
        List<I18nLang> i18nLangsListTemp = i18nLangsList.stream()
                .map(i18nLang -> new I18nLang(i18nLang.getLang(), i18nLang.getLabel()))
                .collect(Collectors.toList());

        i18nEntriesListResult.setI18nLangsList(i18nLangsListTemp);
        i18nEntriesListResult.setMessages(messages);
        return i18nEntriesListResult;
    }

    /**
     * 获取词条宿主支持的语言
     *
     * @return host langs
     */
    @SystemServiceLog(description = "getHostLangs 获取词条宿主支持的语言")
    public List<I18nLang> getHostLangs() {
        // 先默认全部应用都支持中英文, 后续其他语言需要结合管理后台逻辑二次开发
        QueryWrapper<I18nLang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lang", "zh_CN").or().eq("lang", "en_US");
        return i18nLangMapper.selectList(queryWrapper);
    }

    /**
     * 获取格式化词条列表
     *
     * @param i18nEntriesList i18nEntriesList
     * @return map result
     */
    @SystemServiceLog(description = "formatEntriesList 国际化词条实现类里获取格式化词条列表")
    @Override
    public SchemaI18n formatEntriesList(List<I18nEntryDto> i18nEntriesList) {
        // 格式化词条列表
        Map<String, Map<String, String>> messages = new HashMap<>();
        for (I18nEntryDto i18nEntries : i18nEntriesList) {
            String lang = i18nEntries.getLang().getLang();
            String key = i18nEntries.getKey();
            String content = i18nEntries.getContent();
            // 使用三元运算符检查是否存在 lang 对应的 map，如果不存在则创建
            messages.putIfAbsent(lang, new HashMap<>());
            // 现在可以将键值对存入 messages[lang] 对应的 map 中
            messages.get(lang).put(key, content);
        }
        return BeanUtil.mapToBean(messages, SchemaI18n.class, true);
    }

    /**
     * 创建多词条国际化
     *
     * @param operateI18nEntries operateI18nEntries
     * @return I18nEntry
     */
    @Override
    public List<I18nEntry> create(OperateI18nEntries operateI18nEntries) {
        List<I18nLang> langs = getHostLangs();
        Map<String, Integer> langsDic = new HashMap<>();
        for (I18nLang i18nLangs : langs) {
            langsDic.put(i18nLangs.getLang(), i18nLangs.getId());
        }

        List<I18nEntry> i18nEntriesList = fillParam(operateI18nEntries, langsDic);
        i18nEntriesList.stream().map(entry -> i18nEntryMapper.createI18nEntry(entry)).collect(Collectors.toList());
        return i18nEntriesList;
    }

    /**
     * 填充参数
     *
     * @param operateI18nEntries the operate i 18 n entries
     * @param langsDic           the langs dic
     * @return list
     */
    @SystemServiceLog(description = "fillParam 填充参数")
    public List<I18nEntry> fillParam(OperateI18nEntries operateI18nEntries, Map<String, Integer> langsDic) {
        List<I18nEntry> i18nEntriesList = new ArrayList<>();

        Map<String, String> contents = operateI18nEntries.getContents();

        contents.keySet().forEach(item -> {
            int lang = langsDic.get(item);
            if (lang != 0) {
                I18nEntry i18nEntries = new I18nEntry();
                i18nEntries.setHost(Integer.valueOf(operateI18nEntries.getHost()));
                i18nEntries.setKey(operateI18nEntries.getKey());
                i18nEntries.setLang(lang);
                i18nEntries.setHostType(operateI18nEntries.getHostType());
                i18nEntries.setContent(contents.get(item));
                i18nEntriesList.add(i18nEntries);
            }
        });
        return i18nEntriesList;
    }

    /**
     * 批量创建
     *
     * @param operateI18nBatchEntries operateI18nBatchEntries
     * @return I18nEntry
     */
    @SystemServiceLog(description = "bulkCreate 国际化词条批量创建")
    @Override
    public List<I18nEntry> bulkCreate(OperateI18nBatchEntries operateI18nBatchEntries) {
        // getEntriesParam
        List<I18nEntry> i18nEntriesList = getEntriesParam(operateI18nBatchEntries);

        // bulkCreateEntries
        i18nEntriesList.stream().map(entry -> i18nEntryMapper.createI18nEntry(entry)).collect(Collectors.toList());
        return i18nEntriesList;
    }

    /**
     * 获取该词条下多语言的参数
     *
     * @param operateI18NEntries the operate i 18 n entries
     * @return entries param
     */
    @SystemServiceLog(description = "getEntriesParam 获取该词条下多语言的参数")
    public List<I18nEntry> getEntriesParam(OperateI18nBatchEntries operateI18NEntries) {
        List<I18nLang> i18nLangsList = getHostLangs();
        if (i18nLangsList == null) {
            return new ArrayList<>();
        }
        return formatEntriesParam(operateI18NEntries, i18nLangsList);
    }

    /**
     * 格式化词条参数
     *
     * @param operateI18nBatchEntries the operate i 18 n batch entries
     * @param i18nLangsList           the 18 n langs list
     * @return list
     */
    @SystemServiceLog(description = "formatEntriesParam 格式化词条参数")
    public List<I18nEntry> formatEntriesParam(OperateI18nBatchEntries operateI18nBatchEntries,
                                              List<I18nLang> i18nLangsList) {
        Map<String, Integer> langsDic = new HashMap<>();
        List<I18nEntry> i18nEntriesListResult = new ArrayList<>();
        for (I18nLang i18nLangs : i18nLangsList) {
            langsDic.put(i18nLangs.getLang(), i18nLangs.getId());
        }
        // 需要两种类型判断 待定 操作批量国际化
        for (Entry entry : operateI18nBatchEntries.getEntries()) {
            OperateI18nEntries operateI18nEntries = new OperateI18nEntries();
            operateI18nEntries.setHost(operateI18nBatchEntries.getHost());
            operateI18nEntries.setHostType(operateI18nBatchEntries.getHostType());
            operateI18nEntries.setKey(entry.getKey());
            operateI18nEntries.setContents(entry.getContents());
            i18nEntriesListResult.addAll(fillParam(operateI18nEntries, langsDic));
        }

        return i18nEntriesListResult;
    }

    /**
     * 批量更新
     *
     * @param operateI18nEntries operateI18nEntries
     * @return I18nEntry
     */
    @SystemServiceLog(description = "bulkUpdate 批量更新")
    @Override
    public List<I18nEntry> bulkUpdate(OperateI18nEntries operateI18nEntries) {
        if ("app".equals(operateI18nEntries.getHostType())) {
            I18nEntry i18nEntries = new I18nEntry();
            i18nEntries.setHostType(operateI18nEntries.getHostType());
            i18nEntries.setHost(Integer.valueOf(operateI18nEntries.getHost()));
            i18nEntries.setKey(operateI18nEntries.getKey());
            List<I18nEntryDto> i18nEntriesList = i18nEntryMapper.queryI18nEntryByCondition(i18nEntries);
            if (i18nEntriesList.isEmpty()) {
                return create(operateI18nEntries);
            }
        }
        return bulkUpdateEntries(operateI18nEntries);
    }

    /**
     * 批量更新
     *
     * @param operateI18nEntries the operate i 18 n entries
     * @return list
     */
    @SystemServiceLog(description = "bulkUpdateEntries 国际化词条批量更新")
    public List<I18nEntry> bulkUpdateEntries(OperateI18nEntries operateI18nEntries) {
        List<I18nLang> langs = getHostLangs();
        Map<String, Integer> langsDic = new HashMap<>();
        for (I18nLang i18nLangs : langs) {
            langsDic.put(i18nLangs.getLang(), i18nLangs.getId());
        }

        List<I18nEntry> i18nEntriesList = fillParam(operateI18nEntries, langsDic);
        // bulkCreateEntries
        for (I18nEntry i18Entries : i18nEntriesList) {
            i18nEntryMapper.updateByEntry(i18Entries.getContent(), i18Entries.getHost(),
                    i18Entries.getHostType(), i18Entries.getKey(), i18Entries.getLang());
        }
        return i18nEntriesList;
    }

    /**
     * 删除国际化词条
     *
     * @param deleteI18nEntry deleteI18nEntry
     * @return I18nEntry
     */
    @SystemServiceLog(description = "deleteI18nEntriesByHostAndHostTypeAndKey 根据host、host_type、key查询删除国际化词条")
    @Override
    public List<I18nEntryDto> deleteI18nEntriesByHostAndHostTypeAndKey(DeleteI18nEntry deleteI18nEntry) {
        List<I18nEntryDto> i18nEntriesList = new ArrayList<>();
        for (String key : deleteI18nEntry.getKeyIn()) {
            I18nEntry i18nEntry = new I18nEntry();
            i18nEntry.setHostType(deleteI18nEntry.getHostType());
            i18nEntry.setHost(Integer.valueOf(deleteI18nEntry.getHost()));
            i18nEntry.setKey(key);
            List<I18nEntryDto> i18nEntries = i18nEntryMapper.queryI18nEntryByCondition(i18nEntry);
            i18nEntries.stream().forEach(i18nEntriesDto -> i18nEntryMapper.deleteI18nEntryById(i18nEntriesDto.getId()));
            i18nEntriesList.addAll(i18nEntries);
        }
        return i18nEntriesList;
    }

    /**
     * 上传单个文件
     *
     * @param file the file
     * @param host the host
     * @return bulk Create Or Update number
     * @throws Exception the Exception
     */
    @SystemServiceLog(description = "readSingleFileAndBulkCreate 上传单个国际化文件")
    @Override
    public Result<I18nFileResult> readSingleFileAndBulkCreate(MultipartFile file, int host)
            throws Exception {
        List<EntriesItem> entriesArr = new ArrayList<>();
        String contentType = file.getContentType();

        if (Objects.equals(contentType, Enums.MimeType.JSON.getValue())) {
            Result<EntriesItem> parseJsonFileStreamResult = parseJsonFileStream(file);
            if (!parseJsonFileStreamResult.isSuccess()) {
                return Result.failed(ExceptionEnum.CM001);
            }
            entriesArr.add(parseJsonFileStreamResult.getData());
        } else {
            entriesArr.addAll(parseZipFileStream(file));
        }
        // 批量上传接口未提交任何文件流时报错
        if (entriesArr.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM002.getResultCode(), "No file uploaded");
        }


        return bulkCreateOrUpdate(entriesArr, host);
    }

    /**
     * 批量上传词条数据
     *
     * @param file
     * @param host
     */
    @SystemServiceLog(description = "readFilesAndbulkCreate 批量上传词条数据")
    @Override
    public Result<I18nFileResult> readFilesAndbulkCreate(String lang, MultipartFile file, int host) throws Exception {

        List<EntriesItem> entriesArr = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        Result<EntriesItem> parseJsonFileStreamResult = parseJsonFileStream(file);
        // 解析 JSON 数据
        if (!parseJsonFileStreamResult.isSuccess()) {
            return Result.failed(ExceptionEnum.CM001);
        }
        EntriesItem entriesItem = parseJsonFileStreamResult.getData();
        entriesArr.add(entriesItem);
        // 批量上传接口未提交任何文件流时报错
        if (entriesArr.isEmpty()) {
            throw new Exception("No file uploaded");
        }

        I18nFileResult i18nFileResult = bulkCreateOrUpdate(entriesArr, host).getData();
        return Result.success(i18nFileResult);

    }

    /**
     * 批量创建或修改
     *
     * @param entriesArr the entries arr
     * @param host       the host
     * @return map
     */
    @SystemServiceLog(description = "bulkCreateOrUpdate 批量创建或修改")
    public Result<I18nFileResult> bulkCreateOrUpdate(List<EntriesItem> entriesArr, int host) {
        List<I18nEntry> entries = new ArrayList<>();
        entriesArr.forEach(entriesItem -> {
            Map<String, Object> langEntries = entriesItem.getEntries();
            langEntries.forEach((key, value) -> {
                I18nEntry i18nEntry = new I18nEntry();
                i18nEntry.setKey(key);
                i18nEntry.setLang(entriesItem.getLang());
                i18nEntry.setHost(host);
                i18nEntry.setHostType("app");
                i18nEntry.setContent(value.toString());
                entries.add(i18nEntry);
            });
        });
        // 超大量数据更新，如上传国际化文件，不返回插入或更新的词条
        I18nFileResult result = bulkInsertOrUpdate(entries);
        return Result.success(result);
    }


    /**
     * 超大量数据更新，如上传国际化文件，不返回插入或更新的词条
     *
     * @param entries the entries
     * @return the map
     */
    @SystemServiceLog(description = "bulkInsertOrUpdate 超大量数据更新")
    public I18nFileResult bulkInsertOrUpdate(List<I18nEntry> entries) {
        int addNum = 0;
        int updateNum = 0;
        for (I18nEntry entry : entries) {
            // 构建查询条件，假设 key 作为唯一键
            // 查询数据库中是否存在该记录
            I18nEntryDto existingEntry = i18nEntryMapper.findI18nEntriesByKeyAndLang(entry.getKey(), entry.getLang());

            if (existingEntry == null) {
                // 插入新记录
                i18nEntryMapper.createI18nEntry(entry);
                addNum = addNum + 1;
            } else {
                // 更新记录
                entry.setId(existingEntry.getId());
                i18nEntryMapper.updateI18nEntryById(entry);
                updateNum = updateNum + 1;
            }
        }

        // 构造返回插入和更新的条数
        I18nFileResult i18nFileResult = new I18nFileResult();
        i18nFileResult.setInsertNum(addNum);
        i18nFileResult.setUpdateNum(updateNum);
        return i18nFileResult;
    }

    /**
     * 解析JSON文件
     *
     * @param file the file
     * @return result
     */
    public Result<EntriesItem> parseJsonFileStream(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        log.info("Parsing JSON file: {}", fileName);

        // 校验文件流合法性
        validateFileStream(file, ExceptionEnum.CM308.getResultCode(), Arrays.asList(Enums.MimeType.JSON.getValue()));

        // 根据文件名判断 lang value
        EntriesItem entriesItem = setLang(fileName);

        // 解析国际化词条文件
        try {
            // 使用 try-with-resources 自动管理输入流

            byte[] fileBytes = Utils.readAllBytes(file.getInputStream());
            String jsonContent = new String(fileBytes, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonData = objectMapper.readValue(jsonContent, new TypeReference<Map<String, Object>>() {
            });
            entriesItem.setEntries(flat(jsonData));

        } catch (IOException e) {
            log.error("Error parsing JSON: {}", e.getMessage());
            return Result.validateFailed("Error parsing JSON");
        }

        log.info("Successfully parsed JSON file: {}", fileName);
        return Result.success(entriesItem);
    }


    /**
     * 解析zip文件
     *
     * @param file the file
     * @return map
     * @throws Exception the exception
     */
    public List<EntriesItem> parseZipFileStream(MultipartFile file) throws Exception {
        // 校验文件流合法性
        validateFileStream(file, ExceptionEnum.CM314.getResultCode(),
                Arrays.asList(Enums.MimeType.ZIP.getValue(), Enums.MimeType.XZIP.getValue()));

        List<EntriesItem> entriesItems = new ArrayList<>();
        // 解压ZIP文件并处理
        List<FileInfo> fileInfos = Utils.unzip(file);
        ObjectMapper objectMapper = new ObjectMapper();

        for (FileInfo fileInfo : fileInfos) {
            if (!fileInfo.isDirectory()) {
                EntriesItem entriesItem = setLang(fileInfo.getName());

                // 处理 JSON 内容
                try {
                    Map<String, Object> jsonData = objectMapper.readValue(fileInfo.getContent(), new TypeReference<Map<String, Object>>() {
                    });
                    entriesItem.setEntries(flat(jsonData));
                } catch (JsonProcessingException e) {
                    log.error("JSON processing error for file: " + fileInfo.getName(), e);
                    throw new RuntimeException(e);
                }

                entriesItems.add(entriesItem);
            }
        }

        return entriesItems;
    }


    /**
     * 校验文件流合法性
     *
     * @param file      文件
     * @param code      报错码
     * @param mimeTypes 文件类型集合
     */
    public void validateFileStream(MultipartFile file, String code, List<String> mimeTypes) {
        boolean condition = file.getOriginalFilename() != null
                && file.getName().matches("\\d+")
                && mimeTypes.contains(file.getContentType());
        if (condition) {
            return;
        }

        // 只要文件不合法就throw error， 无论是批量还是单个
        try {
            file.getInputStream().close();
        } catch (IOException e) {
            log.error("file close fail:{}", e.getMessage());
        }
        throw new ServiceException(code, "validate file fail");
    }


    /**
     * 根据主键id查询表t_i18n_entry信息
     *
     * @param id id
     * @return I18nEntryDto
     * @throws ServiceException ServiceException
     */
    @Override
    public I18nEntryDto findI18nEntryById(@Param("id") Integer id) throws ServiceException {
        return i18nEntryMapper.queryI18nEntryById(id);
    }

    /**
     * 根据条件查询表t_i18n_entry数据
     *
     * @param i18nEntry i18nEntry
     * @return I18nEntryDto
     * @throws ServiceException ServiceException
     */
    @Override
    public List<I18nEntryDto> findI18nEntryByCondition(I18nEntry i18nEntry) throws ServiceException {
        return i18nEntryMapper.queryI18nEntryByCondition(i18nEntry);
    }

    /**
     * 根据主键id更新表t_i18n_entry数据
     *
     * @param i18nEntry i18nEntry
     * @return execute success data number
     */
    @Override
    public Integer updateI18nEntryById(I18nEntry i18nEntry) {
        return i18nEntryMapper.updateI18nEntryById(i18nEntry);
    }

    /**
     * 根据文件名判断lang值
     *
     * @param fileName the fileName
     * @return EntriesItem the entriesItem
     */
    private EntriesItem setLang(String fileName) {
        EntriesItem entriesItem = new EntriesItem();
        String name;
        int lastDotIndex = fileName.lastIndexOf('.'); // 找到最后一个点的索引
        if (lastDotIndex == -1) {
            name = fileName; // 如果没有扩展名，直接返回文件名
        } else {
            name = fileName.substring(0, lastDotIndex); // 返回不带扩展名的文件名
        }
        if (Enums.I18nFileName.EN_US.getValue().equals(name)) {
            entriesItem.setLang(2);
            return entriesItem;
        } else if (Enums.I18nFileName.ZH_CN.getValue().equals(name)) {
            entriesItem.setLang(1);
            return entriesItem;
        }
        return entriesItem;
    }
}
