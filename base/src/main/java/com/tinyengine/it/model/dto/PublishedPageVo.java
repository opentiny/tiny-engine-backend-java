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

import java.util.List;

/**
 * 页面历史数据
 *
 * @since 2024-10-20
 */
@Setter
@Getter
public class PublishedPageVo {
    private Integer refId;
    /**
     * 名称
     */
    private String name;

    /**
     * 版本
     */
    private List<PageHistoryVo> historyVersions;

    /**
     * 关联app表Id
     */
    private Integer appId;

    /**
     * 页面路由
     */
    private String route;

    /**
     * 父文件夹id
     */
    private String parentId;

    private String group;

    /**
     * 是否为页面：分为页面和文件夹
     */
    private Boolean isPage;

    /**
     * 页面id
     *
     * @return page id
     */
    public Integer getPageId() {
        return this.getRefId();
    }
}
