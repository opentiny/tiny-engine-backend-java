package com.tinyengine.it.service.app.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.I18nLangMapper;
import com.tinyengine.it.model.dto.DeleteI18nEntry;
import com.tinyengine.it.model.dto.EntriesItem;
import com.tinyengine.it.model.dto.Entry;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.I18nEntryListResult;
import com.tinyengine.it.model.dto.I18nFileResult;
import com.tinyengine.it.model.dto.OperateI18nBatchEntries;
import com.tinyengine.it.model.dto.OperateI18nEntries;
import com.tinyengine.it.model.dto.SchemaI18n;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.model.entity.I18nLang;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
class I18nEntryServiceImplTest {
    @Mock
    private I18nEntryMapper i18nEntryMapper;
    @Mock
    private I18nLangMapper i18nLangMapper;
    @InjectMocks
    private I18nEntryServiceImpl i18nEntryServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindAllI18nEntry() {
        I18nEntryDto i18nEntryDto = new I18nEntryDto();
        List<I18nEntryDto> i18nEntryDtos = Arrays.<I18nEntryDto>asList(i18nEntryDto);
        I18nLang lang = new I18nLang("zh_CN", "label");
        i18nEntryDto.setLang(lang);
        i18nEntryDto.setKey("key");
        i18nEntryDto.setContent("content");
        when(i18nEntryMapper.queryAllI18nEntry()).thenReturn(i18nEntryDtos);

        I18nEntry i18nEntry = new I18nEntry();

        List<I18nEntry> mockEntry = Arrays.asList(i18nEntry);
        when(i18nEntryMapper.selectList(any(Wrapper.class))).thenReturn(mockEntry);

        List<I18nLang> mockLang = Arrays.asList(new I18nLang("zh_CN", "label"));
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockLang);

        I18nEntryListResult result = i18nEntryServiceImpl.findAllI18nEntry();
        Assertions.assertEquals("content", result.getMessages().getZh_CN().get("key"));
    }

    @Test
    void testGetHostLangs() {
        I18nEntry i18nEntry = new I18nEntry();
        List<I18nEntry> mockData = Arrays.asList(i18nEntry);
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockData);

        List<I18nLang> result = i18nEntryServiceImpl.getHostLangs();
        Assertions.assertEquals(i18nEntry, result.get(0));
    }

    @Test
    void testFormatEntriesList() {
        I18nEntryDto i18nEntryDto = new I18nEntryDto();
        I18nLang lang = new I18nLang("zh_CN", "label");
        i18nEntryDto.setLang(lang);
        i18nEntryDto.setKey("key");
        i18nEntryDto.setContent("content");
        List<I18nEntryDto> i18nEntryDtos = Arrays.asList(i18nEntryDto);

        SchemaI18n result = i18nEntryServiceImpl.formatEntriesList(i18nEntryDtos);
        Assertions.assertEquals("content", result.getZh_CN().get("key"));
    }

    @Test
    void testCreate() {
        I18nLang i18nLang = new I18nLang("zh_CN", "label");
        i18nLang.setId(1);
        List<I18nLang> mockLang = Arrays.asList(i18nLang);
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockLang);

        when(i18nEntryMapper.createI18nEntry(any(I18nEntry.class))).thenReturn(1);

        OperateI18nEntries param = new OperateI18nEntries();
        HashMap<String, String> contents = new HashMap<>();
        contents.put("zh_CN", "content");
        param.setContents(contents);
        param.setHost("1");
        List<I18nEntry> result = i18nEntryServiceImpl.create(param);
        Assertions.assertEquals("content", result.get(0).getContent());
    }

    @Test
    void testBulkCreate() {
        I18nLang i18nLang = new I18nLang("zh_CN", "label");
        i18nLang.setId(1);
        List<I18nLang> mockLang = Arrays.asList(i18nLang);
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockLang);

        OperateI18nBatchEntries param = new OperateI18nBatchEntries();
        ArrayList<Entry> entries = new ArrayList<>();
        Entry entry = new Entry();
        entry.setKey("key");
        HashMap<String, String> contents = new HashMap<>();
        contents.put("zh_CN", "text contents");
        entry.setContents(contents);
        entries.add(entry);
        param.setEntries(entries);
        param.setHost("1");
        List<I18nEntry> result = i18nEntryServiceImpl.bulkCreate(param);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("key", result.get(0).getKey());
        Assertions.assertEquals("text contents", result.get(0).getContent());
        Assertions.assertEquals(1, result.get(0).getHost());
    }

    @Test
    void testGetEntriesParam() {
        I18nLang i18nLang = new I18nLang("zh_CN", "label");
        i18nLang.setId(1);
        List<I18nLang> mockLang = Arrays.asList(i18nLang);
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockLang);

        when(i18nEntryMapper.selectList(any(Wrapper.class))).thenReturn(Arrays.asList(new I18nEntry()));

        OperateI18nBatchEntries param = new OperateI18nBatchEntries();
        ArrayList<Entry> entries = new ArrayList<>();
        Entry entry = new Entry();
        entry.setKey("key");
        HashMap<String, String> contents = new HashMap<>();
        contents.put("zh_CN", "text contents");
        entry.setContents(contents);
        entries.add(entry);
        param.setEntries(entries);
        param.setHost("2");
        List<I18nEntry> result = i18nEntryServiceImpl.getEntriesParam(param);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("key", result.get(0).getKey());
        Assertions.assertEquals("text contents", result.get(0).getContent());
        Assertions.assertEquals(2, result.get(0).getHost());
    }

    @Test
    void testBulkUpdateForApp() {
        List<I18nEntryDto> entryDtoList = Arrays.asList(new I18nEntryDto());
        when(i18nEntryMapper.queryI18nEntryByCondition(any(I18nEntry.class))).thenReturn(entryDtoList);

        I18nLang i18nLang = new I18nLang("zh_CN", "label");
        i18nLang.setId(1);
        List<I18nLang> mockLang = Arrays.asList(i18nLang);
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockLang);

        when(i18nEntryMapper.createI18nEntry(any(I18nEntry.class))).thenReturn(Integer.valueOf(0));
        when(i18nEntryMapper.updateByEntry(anyString(), anyInt(), anyString(), anyString(), anyInt()))
                .thenReturn(Integer.valueOf(0));

        OperateI18nEntries param = new OperateI18nEntries();
        HashMap<String, String> contents = new HashMap<>();
        contents.put("zh_CN", "content");
        param.setContents(contents);
        param.setHostType("app");
        param.setHost("1");

        List<I18nEntry> result = i18nEntryServiceImpl.bulkUpdate(param);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("content", result.get(0).getContent());
    }

    @Test
    void testBulkUpdateWhenNotApp() {
        List<I18nEntryDto> entryDtoList = Arrays.asList(new I18nEntryDto());
        when(i18nEntryMapper.queryI18nEntryByCondition(any(I18nEntry.class))).thenReturn(entryDtoList);

        I18nLang i18nLang = new I18nLang("zh_CN", "label");
        i18nLang.setId(1);
        List<I18nLang> mockLang = Arrays.asList(i18nLang);
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockLang);

        when(i18nEntryMapper.createI18nEntry(any(I18nEntry.class))).thenReturn(Integer.valueOf(0));
        when(i18nEntryMapper.updateByEntry(anyString(), anyInt(), anyString(), anyString(), anyInt()))
                .thenReturn(Integer.valueOf(0));

        OperateI18nEntries param = new OperateI18nEntries();
        HashMap<String, String> contents = new HashMap<>();
        contents.put("zh_CN", "content");
        param.setContents(contents);
        param.setHost("1");

        List<I18nEntry> result = i18nEntryServiceImpl.bulkUpdate(param);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("content", result.get(0).getContent());
    }

    @Test
    void testBulkUpdateEntries() {
        I18nLang i18nLang = new I18nLang("zh_CN", "label");
        i18nLang.setId(1);
        List<I18nLang> mockLang = Arrays.asList(i18nLang);
        when(i18nLangMapper.selectList(any(Wrapper.class))).thenReturn(mockLang);

        OperateI18nEntries param = new OperateI18nEntries();
        HashMap<String, String> contents = new HashMap<>();
        contents.put("zh_CN", "content");
        param.setContents(contents);
        param.setHost("1");
        List<I18nEntry> result = i18nEntryServiceImpl.bulkUpdateEntries(param);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("content", result.get(0).getContent());
    }

    @Test
    void testDeleteI18nEntriesByHostAndHostTypeAndKey() {
        List<I18nEntryDto> mockData = Arrays.asList(new I18nEntryDto());
        when(i18nEntryMapper.queryI18nEntryByCondition(any(I18nEntry.class))).thenReturn(mockData);

        DeleteI18nEntry param = new DeleteI18nEntry();
        ArrayList<String> keyIn = new ArrayList<>();
        keyIn.add("");
        param.setKeyIn(keyIn);
        param.setHost("1");
        List<I18nEntryDto> result = i18nEntryServiceImpl.deleteI18nEntriesByHostAndHostTypeAndKey(param);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testReadSingleFileAndBulkCreate() throws Exception {
        when(i18nEntryMapper.updateI18nEntryById(any(I18nEntry.class))).thenReturn(1);
        when(i18nEntryMapper.createI18nEntry(any(I18nEntry.class))).thenReturn(1);
        when(i18nEntryMapper.findI18nEntriesByKeyAndLang(anyString(), anyInt())).thenReturn(new I18nEntryDto());

        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/json");
        when(file.getOriginalFilename()).thenReturn("originalName");
        when(file.getName()).thenReturn("123");
        when(file.getBytes()).thenReturn("{\"name\":\"value\"}".getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(IoUtil.toStream("test".getBytes(StandardCharsets.UTF_8)));

        Result<I18nFileResult> result = i18nEntryServiceImpl.readSingleFileAndBulkCreate(file, 0);

        Assertions.assertNull(result.getData());
        Assertions.assertFalse(result.isSuccess());
    }

    @Test
    void testReadFilesAndbulkCreate() throws Exception {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/json");
        when(file.getOriginalFilename()).thenReturn("originalName");
        when(file.getName()).thenReturn("123");
        when(file.getBytes()).thenReturn("{\"name\":\"value\"}".getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(IoUtil.toStream("test".getBytes(StandardCharsets.UTF_8)));
        // file not existed
        Result<I18nFileResult> result = i18nEntryServiceImpl.readFilesAndbulkCreate("1", file, 0);
        Assertions.assertNull(result.getData());
        Assertions.assertFalse(result.isSuccess());
    }

    @Test
    void testBulkInsertOrUpdate() {
        when(i18nEntryMapper.updateI18nEntryById(any(I18nEntry.class))).thenReturn(Integer.valueOf(0));
        when(i18nEntryMapper.createI18nEntry(any(I18nEntry.class))).thenReturn(Integer.valueOf(0));

        I18nEntryDto entryDto = new I18nEntryDto();
        when(i18nEntryMapper.findI18nEntriesByKeyAndLang(anyString(), anyInt())).thenReturn(entryDto);

        I18nEntry i18nEntry = new I18nEntry();
        i18nEntry.setLang(1);
        List<I18nEntry> entryList = Arrays.asList(i18nEntry);
        I18nFileResult result = i18nEntryServiceImpl.bulkInsertOrUpdate(entryList);
        verify(i18nEntryMapper, times(1)).createI18nEntry(i18nEntry);
        Assertions.assertEquals(1, result.getInsertNum());
        Assertions.assertEquals(0, result.getUpdateNum());
    }

    @Test
    void testParseJsonFileStream() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/json");
        when(file.getOriginalFilename()).thenReturn("originalName");
        when(file.getName()).thenReturn("123");
        when(file.getBytes()).thenReturn("{\"name\":\"value\"}".getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(IoUtil.toStream("test".getBytes(StandardCharsets.UTF_8)));

        Result<EntriesItem> result = i18nEntryServiceImpl.parseJsonFileStream(file);
        assertFalse(result.isSuccess());
    }

    @Test
    void testValidateFileStream() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/json");
        when(file.getOriginalFilename()).thenReturn("originalName");
        when(file.getName()).thenReturn("123");

        i18nEntryServiceImpl.validateFileStream(file, "code", Arrays.asList("application/json"));
        verify(file, times(0)).getInputStream();
    }

    @Test
    void testFindI18nEntryById() {
        int paramId = 1;
        I18nEntryDto mockData = new I18nEntryDto();
        when(i18nEntryMapper.queryI18nEntryById(paramId)).thenReturn(mockData);

        I18nEntryDto result = i18nEntryServiceImpl.findI18nEntryById(paramId);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindI18nEntryByCondition() {
        I18nEntry param = new I18nEntry();
        List<I18nEntryDto> mockData = Arrays.asList(new I18nEntryDto());
        when(i18nEntryMapper.queryI18nEntryByCondition(param)).thenReturn(mockData);

        List<I18nEntryDto> result = i18nEntryServiceImpl.findI18nEntryByCondition(param);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testUpdateI18nEntryById() {
        I18nEntry param = new I18nEntry();
        when(i18nEntryMapper.updateI18nEntryById(param)).thenReturn(1);

        Integer result = i18nEntryServiceImpl.updateI18nEntryById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void parseZipFileStream() throws Exception {
        MultipartFile file = new MockMultipartFile("123", "originalName",
                "application/zip", "{\"name\":\"value\"}".getBytes(StandardCharsets.UTF_8));

        try (MockedStatic<JSONUtil> jsonUtil = Mockito.mockStatic(JSONUtil.class)) {
            HashMap<String, Object> objectHashMap = new HashMap<>();
            objectHashMap.put("key", "value");
            TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
            jsonUtil.when(() -> JSONUtil.toBean("", typeReference.getType(), true))
                    .thenReturn(objectHashMap);
            List<EntriesItem> result = i18nEntryServiceImpl.parseZipFileStream(file);

            assertEquals(0, result.size());
        }
    }
}

