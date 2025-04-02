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

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.ComponentLibraryMapper;
import com.tinyengine.it.model.entity.ComponentLibrary;
import com.tinyengine.it.service.material.ComponentLibraryService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type ComponentLibrary service.
 *
 * @since 2025-04-02
 */
@Service
@Slf4j
public class ComponentLibraryServiceImpl implements ComponentLibraryService {
    @Autowired
    private ComponentLibraryMapper componentLibraryMapper;

    /**
     * 查询表t_component_library所有数据
     *
     * @return ComponentLibrary
     */
    @Override
    public List<ComponentLibrary> queryAllComponentLibrary() {
        return componentLibraryMapper.queryAllComponentLibrary();
    }

    /**
     * 根据主键id查询表t_component_library信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Result<ComponentLibrary> queryComponentLibraryById(@Param("id") Integer id) {
        ComponentLibrary material = componentLibraryMapper.queryComponentLibraryById(id);
        if(null == material.getId()){
            return Result.failed(ExceptionEnum.CM009);
        }
        return Result.success(material);
    }

    /**
     * 根据条件查询表t_component_library数据
     *
     * @param material material
     * @return query result
     */
    @Override
    public List<ComponentLibrary> queryComponentLibraryByCondition(ComponentLibrary material) {
        return componentLibraryMapper.queryComponentLibraryByCondition(material);
    }

    /**
     * 根据主键id删除表t_component_library数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public  Result<ComponentLibrary> deleteComponentLibraryById(@Param("id") Integer id) {
        int deleteResult =  componentLibraryMapper.deleteComponentLibraryById(id);
        if(deleteResult != 1){
            return Result.failed(ExceptionEnum.CM008);
        }
        Result<ComponentLibrary> result = this.queryComponentLibraryById(id);
        return result;

    }

    /**
     * 根据主键id更新表t_component_library数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public  Result<ComponentLibrary> updateComponentLibraryById(ComponentLibrary material) {
        int updateResult = componentLibraryMapper.updateComponentLibraryById(material);
        if(updateResult != 1){
            return Result.failed(ExceptionEnum.CM008);
        }
        Result<ComponentLibrary> result = this.queryComponentLibraryById(material.getId());
        return result;
    }

    /**
     * 新增表t_component_library数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public  Result<ComponentLibrary> createComponentLibrary(ComponentLibrary material) {
        int createResult = componentLibraryMapper.createComponentLibrary(material);
        if(createResult != 1){
            return Result.failed(ExceptionEnum.CM008);
        }
        Result<ComponentLibrary> result = this.queryComponentLibraryById(material.getId());
        return result;
    }
}
