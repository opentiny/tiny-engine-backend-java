package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.service.app.I18nEntryService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class I18nEntryControllerTest {
    @Mock
    private I18nEntryService i18nEntryService;
    @InjectMocks
    private I18nEntryController i18nEntryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllI18nEntries() {
        I18nEntryListResult mockData = new I18nEntryListResult();
        when(i18nEntryService.findAllI18nEntry()).thenReturn(mockData);

        Result<I18nEntryListResult> result = i18nEntryController.getAllI18nEntries();
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testGetI18nEntriesById() {
        I18nEntryDto i18nEntry = new I18nEntryDto();
        when(i18nEntryService.findI18nEntryById(1)).thenReturn(i18nEntry);

        Result<I18nEntryDto> result = i18nEntryController.getI18nEntriesById(1);
        Assertions.assertEquals(i18nEntry, result.getData());
    }

    @Test
    void testCreateI18nEntries() {
        I18nEntry i18nEntry = new I18nEntry();
        List<I18nEntry> mockData = Arrays.<I18nEntry>asList(i18nEntry);
        when(i18nEntryService.create(any(OperateI18nEntries.class))).thenReturn(mockData);

        Result<List<I18nEntry>> result = i18nEntryController.createI18nEntries(new OperateI18nEntries());
        Assertions.assertEquals(i18nEntry, result.getData().get(0));
    }

    @Test
    void testBatchCreateEntries() {
        I18nEntry i18nEntry = new I18nEntry();
        when(i18nEntryService.bulkCreate(any(OperateI18nBatchEntries.class))).thenReturn(Arrays.<I18nEntry>asList(i18nEntry));

        Result<List<I18nEntry>> result = i18nEntryController.batchCreateEntries(new OperateI18nBatchEntries());
        Assertions.assertEquals(i18nEntry, result.getData().get(0));
    }

    @Test
    void testUpdateI18nEntries() {
        I18nEntryDto i18nEntry = new I18nEntryDto();
        when(i18nEntryService.findI18nEntryById(1)).thenReturn(i18nEntry);
        when(i18nEntryService.updateI18nEntryById(any(I18nEntry.class))).thenReturn(1);

        Result<I18nEntryDto> result = i18nEntryController.updateI18nEntries(1, new I18nEntry());
        Assertions.assertEquals(i18nEntry, result.getData());
    }

    @Test
    void testUpdateEntry() {
        I18nEntry i18nEntry = new I18nEntry();
        List<I18nEntry> mockData = Arrays.<I18nEntry>asList(i18nEntry);
        when(i18nEntryService.bulkUpdate(any(OperateI18nEntries.class))).thenReturn(mockData);

        Result<List<I18nEntry>> result = i18nEntryController.updateEntry(new OperateI18nEntries());
        Assertions.assertEquals(i18nEntry, result.getData().get(0));
    }

    @Test
    void testDeleteI18nEntries() {
        List<I18nEntryDto> i18nEntryDtos = Arrays.<I18nEntryDto>asList(new I18nEntryDto());
        when(i18nEntryService.deleteI18nEntriesByHostAndHostTypeAndKey(any(DeleteI18nEntry.class)))
                .thenReturn(i18nEntryDtos);

        Result<List<I18nEntryDto>> result = i18nEntryController.deleteI18nEntries(new DeleteI18nEntry());
        Assertions.assertEquals(i18nEntryDtos, result.getData());
    }

    @Test
    void testUpdateI18nSingleFile() throws Exception {
        Result<I18nFileResult> mockData = new Result<>();
        mockData.setSuccess(true);
        when(i18nEntryService.readSingleFileAndBulkCreate(any(MultipartFile.class), anyInt()))
                .thenReturn(mockData);
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        HashMap<String, MultipartFile> filesMap = new HashMap<String, MultipartFile>() {{
            put("filesMap", file);
        }};
        Result<I18nFileResult> result = i18nEntryController.updateI18nSingleFile(1, filesMap);
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testUpdateI18nMultiFile() throws Exception {
        when(i18nEntryService.readFilesAndbulkCreate(anyString(), any(MultipartFile.class), anyInt()))
                .thenReturn(new Result<I18nFileResult>());
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        HashMap<String, MultipartFile> filesMap = new HashMap<String, MultipartFile>() {{
            put("filesMap", file);
        }};
        Result<I18nFileResult> result = i18nEntryController.updateI18nMultiFile(1, filesMap);
        Assertions.assertEquals(new Result<Map<String, Object>>(), result);
    }
}

