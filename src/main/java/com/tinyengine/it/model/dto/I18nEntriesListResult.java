package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.I18nLangs;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class I18nEntriesListResult {
    private Map<String, Map<String, String>> messages;
    private List<I18nLangs> i18nLangsList;

}
