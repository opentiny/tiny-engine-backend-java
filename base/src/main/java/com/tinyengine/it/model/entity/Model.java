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
import com.tinyengine.it.common.handler.ListTypeHandler;
import com.tinyengine.it.model.dto.MethodDto;
import com.tinyengine.it.model.dto.ParametersDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 模型表
 *
 * @since 2025-07-17
 */
@Getter
@Setter
@TableName("t_model")
@Schema(name = "Model", description = "模型表")
public class Model extends BaseEntity {

    @Schema(name = "name_cn", description = "中文名称")
    private String nameCn;

    @Schema(name = "name_En", description = "英文名称")
    private String nameEn;

    @Schema(name = "version", description = "版本")
    private String version;

    @Schema(name = "model_url", description = "模型Url")
    private String modelUrl;

    @Schema(name = "parameters", description = "字段参数")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<ParametersDto> parameters;

    @Schema(name = "method", description = "方法")
    @TableField(typeHandler = ListTypeHandler.class)
    private  List<MethodDto> method;

    @Schema(name = "description", description = "描述")
    private String description;

}
