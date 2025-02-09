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

package com.tinyengine.it.service.material;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.FileResult;
import com.tinyengine.it.model.entity.Component;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The interface Component service.
 *
 * @since 2024-10-20
 */
public interface ComponentService {
    /**
     * 查询表t_component所有信息
     *
     * @return the list
     */
    List<Component> findAllComponent();

    /**
     * 根据主键id查询表t_component信息
     *
     * @param id the id
     * @return the component
     */
    Component findComponentById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_component信息
     *
     * @param component the component
     * @return the list
     */
    List<Component> findComponentByCondition(Component component);

    /**
     * 根据主键id删除t_component数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteComponentById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_component信息
     *
     * @param component the component
     * @return the integer
     */
    Integer updateComponentById(Component component);

    /**
     * 新增表t_component数据
     *
     * @param component the component
     * @return the integer
     */
    Integer createComponent(Component component);

    /**
     * 通过bundle.json新增表t_component数据
     *
     * @param file the file
     * @return result the result
     */
    Result<FileResult> readFileAndBulkCreate(MultipartFile file);
}
