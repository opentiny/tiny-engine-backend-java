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

package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 设计器历史表
 *
 * @since 2025-05-09
 */
@Getter
@Setter
@TableName("t_platform_history")
@Schema(name = "PlatformHistory", description = "设计器历史表")
public class PlatformHistory extends BaseEntity {
    @Schema(name = "refId", description = "设计器id")
    private Integer refId;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "publishUrl", description = "设计器静态资源托管地址")
    private String publishUrl;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "version", description = "当前历史记录表版本")
    private String version;

    @Schema(name = "subCount", description = "设计预留字段")
    private Integer subCount;

    @Schema(name = "materialHistoryId", description = "关联物料包历史ID")
    private Integer materialHistoryId;

    @Schema(name = "imageUrl", description = "设计器截图地址")
    private String imageUrl;

    @Schema(name = "materialPkgName", description = "物料包名称")
    private String materialPkgName;

    @Schema(name = "materialVersion", description = "物料包版本")
    private String  materialVersion;

    @Schema(name = "vscodeUrl", description = "*设计预留字段*")
    private String vscodeUrl;
}
