package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class MetaDto {
    private App app;
    private List<I18nEntryDto> i18n;
    private List<Datasource> source;
    private List<AppExtension> extension;
    private List<PageDto> pages;
    private List<BlockHistory> blockHistories;
    private MaterialHistory materialHistory;
}
