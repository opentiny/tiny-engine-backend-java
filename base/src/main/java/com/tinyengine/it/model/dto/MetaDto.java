/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

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
 *
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
