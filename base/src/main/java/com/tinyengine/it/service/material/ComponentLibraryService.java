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

import com.baomidou.mybatisplus.extension.service.IService;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.ComponentLibrary;

import java.util.List;

/**
 * The interface ComponentLibrary service.
 *
 * @since 2025-4-02
 */
public interface ComponentLibraryService extends IService<ComponentLibrary> {
    /**
     * 查询表t_component_library所有信息
     *
     * @return the list
     */
    List<ComponentLibrary> queryAllComponentLibrary();

    /**
     * 根据主键id查询表t_component_library信息
     *
     * @param id the id
     * @return the material
     */
    Result<ComponentLibrary> queryComponentLibraryById(Integer id);

    /**
     * 根据条件查询表t_component_library信息
     *
     * @param componentLibrary the componentLibrary
     * @return the list
     */
    List<ComponentLibrary> queryComponentLibraryByCondition(ComponentLibrary componentLibrary);

    /**
     * 根据主键id删除t_component_library数据
     *
     * @param id the id
     * @return the integer
     */
    Result<ComponentLibrary> deleteComponentLibraryById(Integer id);

    /**
     * 根据主键id更新表t_component_library信息
     *
     * @param componentLibrary the componentLibrary
     * @return the integer
     */
    Result<ComponentLibrary> updateComponentLibraryById(ComponentLibrary componentLibrary);

    /**
     * 新增表t_component_library数据
     *
     * @param componentLibrary the componentLibrary
     * @return the integer
     */
    Result<ComponentLibrary> createComponentLibrary(ComponentLibrary componentLibrary);
}
