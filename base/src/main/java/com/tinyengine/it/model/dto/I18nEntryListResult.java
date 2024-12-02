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

import com.tinyengine.it.model.entity.I18nLang;

import lombok.Data;

import java.util.List;

/**
 * The type 18 n entry list result.
 *
 * @since 2024-10-20
 */
@Data
public class I18nEntryListResult {
    private SchemaI18n messages;
    private List<I18nLang> i18nLangsList;
}
