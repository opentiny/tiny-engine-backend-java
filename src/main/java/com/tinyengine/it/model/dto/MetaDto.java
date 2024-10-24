package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.model.entity.MaterialHistory;
import com.tinyengine.it.model.entity.Page;
import lombok.Data;

import java.util.List;

/**
 * The type Meta dto.
 * @since 2024-10-20
 */
@Data
public class MetaDto {
    private App app;
    private List<I18nEntryDto> i18n;
    private List<Datasource> source;
    private List<AppExtension> extension;
    private List<Page> pages;
    private List<BlockHistory> blockHistories;
    private MaterialHistory materialHistory;
}
