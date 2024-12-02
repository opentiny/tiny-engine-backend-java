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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.handler.MapTypeHandler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * <p>
 * 应用扩展
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_app_extension")
@Schema(name = "AppExtension", description = "应用扩展")
public class AppExtension extends BaseEntity {
    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "type", description = "类型：npm, function")
    private String type;

    @Schema(name = "content", description = "内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> content;

    @Schema(name = "app", description = "关联app表Id")
    private Integer app;

    @Schema(name = "category", description = "分类：utils,bridge")
    private String category;
}
