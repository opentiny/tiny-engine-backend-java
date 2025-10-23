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

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Model;

import java.util.List;

/**
 * The interface Model mapper.
 *
 * @since 2025-07-17
 */
public interface ModelMapper  extends BaseMapper<Model> {
    /**
     * 新增表t_model数据
     *
     * @param model the model
     * @return the int
     */
    int createModel(Model model);

    /**
     * 修改表t_model数据
     *
     * @param model the model
     * @return the int
     */
    int updateModelById(Model model);

    /**
     * 根据条件查询表t_model数据
     *
     * @param model the model
     * @return model list
     */
    List<Model> queryModelByCondition(Model model);
}
