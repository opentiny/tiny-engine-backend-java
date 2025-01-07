/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 页面历史数据
 *
 * @since 2024-10-20
 */
@Setter
@Getter
public class PageHistoryVo {
    /**
     * 名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * Id
     */
    private Integer id;

    private Integer refId;
}
