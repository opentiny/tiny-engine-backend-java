package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.I18nLang;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The type 18 n entry list result.
 */
@Data
public class I18nEntryListResult {
    private Map<String, Map<String, String>> messages;
    private List<I18nLang> i18nLangsList;

}
