package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class MetaDto {
    private Apps app;
    private List<I18nEntriesDto> i18n;
    private List<Sources> source;
    private List<AppExtensions> extension;
    private List<PageDto> pages;
    private List<BlockHistories> blockHistories;
    private MaterialHistories materialHistory;
}
