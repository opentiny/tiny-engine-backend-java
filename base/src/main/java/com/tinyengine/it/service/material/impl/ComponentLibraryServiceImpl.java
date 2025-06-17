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

package com.tinyengine.it.service.material.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.ComponentLibraryMapper;
import com.tinyengine.it.model.entity.ComponentLibrary;
import com.tinyengine.it.service.material.ComponentLibraryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type ComponentLibrary service.
 *
 * @since 2025-04-02
 */
@Service
@Slf4j
public class ComponentLibraryServiceImpl extends ServiceImpl<ComponentLibraryMapper, ComponentLibrary> implements ComponentLibraryService {
    /**
     * 查询表t_component_library所有数据
     *
     * @return ComponentLibrary
     */
    @Override
    public List<ComponentLibrary> queryAllComponentLibrary() {
        return baseMapper.queryAllComponentLibrary();
    }

    /**
     * 根据主键id查询表t_component_library信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Result<ComponentLibrary> queryComponentLibraryById(Integer id) {
        ComponentLibrary material = baseMapper.queryComponentLibraryById(id);
        return Result.success(material);
    }

    /**
     * 根据条件查询表t_component_library数据
     *
     * @param componentLibrary componentLibrary
     * @return query result
     */
    @Override
    public List<ComponentLibrary> queryComponentLibraryByCondition(ComponentLibrary componentLibrary) {
        return baseMapper.queryComponentLibraryByCondition(componentLibrary);
    }

    /**
     * 根据主键id删除表t_component_library数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Result<ComponentLibrary> deleteComponentLibraryById(Integer id) {
        Result<ComponentLibrary> result = this.queryComponentLibraryById(id);
        if (result.getData() == null || result.getData().getId() == null) {
            return Result.success();
        }
        int deleteResult = baseMapper.deleteComponentLibraryById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return result;

    }

    /**
     * 根据主键id更新表t_component_library数据
     *
     * @param componentLibrary componentLibrary
     * @return execute success data number
     */
    @Override
    public Result<ComponentLibrary> updateComponentLibraryById(ComponentLibrary componentLibrary) {
        int updateResult = baseMapper.updateComponentLibraryById(componentLibrary);
        if (updateResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return this.queryComponentLibraryById(componentLibrary.getId());
    }

    /**
     * 新增表t_component_library数据
     *
     * @param componentLibrary componentLibrary
     * @return execute success data number
     */
    @Override
    public Result<ComponentLibrary> createComponentLibrary(ComponentLibrary componentLibrary) {
        int createResult = baseMapper.createComponentLibrary(componentLibrary);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return this.queryComponentLibraryById(componentLibrary.getId());
    }
}
