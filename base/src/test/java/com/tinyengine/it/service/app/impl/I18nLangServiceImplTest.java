package com.tinyengine.it.service.app.impl;

import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.I18nLangMapper;
import com.tinyengine.it.model.entity.I18nLang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class I18nLangServiceImplTest {
    @Mock
    private I18nLangMapper i18nLangMapper;
    @InjectMocks
    private I18nLangServiceImpl i18nLangServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllI18nLang() {
        I18nLang i18nLang = new I18nLang("lang", "label");
        List<I18nLang> mockData = Arrays.asList(i18nLang);
        when(i18nLangMapper.queryAllI18nLang()).thenReturn(mockData);

        List<I18nLang> result = i18nLangServiceImpl.queryAllI18nLang();
        Assertions.assertEquals(i18nLang, result.get(0));
    }

    @Test
    void testQueryI18nLangById() {
        I18nLang mockData = new I18nLang("lang", "label");
        when(i18nLangMapper.queryI18nLangById(1)).thenReturn(mockData);

        I18nLang result = i18nLangServiceImpl.queryI18nLangById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryI18nLangByCondition() {
        I18nLang i18nLang = new I18nLang("lang", "label");
        when(i18nLangMapper.queryI18nLangByCondition(i18nLang)).thenReturn(Arrays.asList(i18nLang));

        List<I18nLang> result = i18nLangServiceImpl.queryI18nLangByCondition(i18nLang);
        Assertions.assertEquals(i18nLang, result.get(0));
    }

    @Test
    void testDeleteI18nLangById() {
        when(i18nLangMapper.deleteI18nLangById(2)).thenReturn(1);

        Integer result = i18nLangServiceImpl.deleteI18nLangById(2);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testUpdateI18nLangById() {
        I18nLang param = new I18nLang("lang", "label");
        when(i18nLangMapper.updateI18nLangById(param)).thenReturn(1);

        Integer result = i18nLangServiceImpl.updateI18nLangById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateI18nLang() {
        I18nLang param = new I18nLang("lang", "label");
        when(i18nLangMapper.createI18nLang(param)).thenReturn(1);

        Integer result = i18nLangServiceImpl.createI18nLang(param);
        Assertions.assertEquals(1, result);
    }
}
