package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.utils.Utils;
import com.tinyengine.it.mapper.ComponentMapper;
import com.tinyengine.it.model.dto.BundleDto;
import com.tinyengine.it.model.dto.Child;
import com.tinyengine.it.model.dto.FileResult;
import com.tinyengine.it.model.dto.JsonFile;
import com.tinyengine.it.model.dto.Snippet;
import com.tinyengine.it.model.entity.Component;
import com.tinyengine.it.model.entity.MaterialComponent;
import com.tinyengine.it.model.entity.MaterialHistoryComponent;
import com.tinyengine.it.service.material.ComponentService;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The type Component service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class ComponentServiceImpl implements ComponentService {
    @Autowired
    private ComponentMapper componentMapper;

    /**
     * 查询表t_component所有数据
     *
     * @return Component
     */
    @Override
    public List<Component> findAllComponent() {
        return componentMapper.queryAllComponent();
    }

    /**
     * 根据主键id查询表t_component信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Component findComponentById(@Param("id") Integer id) {
        return componentMapper.queryComponentById(id);
    }

    /**
     * 根据条件查询表t_component数据
     *
     * @param component component
     * @return query result
     */
    @Override
    public List<Component> findComponentByCondition(Component component) {
        return componentMapper.queryComponentByCondition(component);
    }

    /**
     * 根据主键id删除表t_component数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteComponentById(@Param("id") Integer id) {
        return componentMapper.deleteComponentById(id);
    }

    /**
     * 根据主键id更新表t_component数据
     *
     * @param component component
     * @return execute success data number
     */
    @Override
    public Integer updateComponentById(Component component) {
        return componentMapper.updateComponentById(component);
    }

    /**
     * 新增表t_component数据
     *
     * @param component component
     * @return execute success data number
     */
    @Override
    public Integer createComponent(Component component) {
        return componentMapper.createComponent(component);
    }

    /**
     * 通过bundle.json新增表t_component数据
     *
     * @param file the file
     * @return result the result
     */
    @Override
    public Result<FileResult> readFileAndBulkCreate(MultipartFile file) {
        // 获取bundle.json数据
        Result<JsonFile> result = Utils.parseJsonFileStream(file);
        if (!result.isSuccess()) {
            return Result.failed(ExceptionEnum.CM001);
        }
        JsonFile jsonFile = result.getData();

        // 获取组件数据
        Object dataObj = jsonFile.getFileContent().get("data");
        Map<String, Object> data = new HashMap<>();

        if (dataObj instanceof Map) {
            data = (Map<String, Object>) dataObj;
        }
        BundleDto bundleDto = BeanUtil.mapToBean(data, BundleDto.class, true);

        List<Map<String, Object>> components = bundleDto.getMaterials().getComponents();
        List<Child> snippets = bundleDto.getMaterials().getSnippets();

        if (components == null || components.isEmpty()) {
            return Result.failed(ExceptionEnum.CM009);
        }
        List<Component> componentList = new ArrayList<>();
        for (Map<String, Object> comp : components) {
            Component component = BeanUtil.mapToBean(comp, Component.class, true);
            component.setId(null);
            component.setIsDefault(true);
            component.setIsOfficial(true);
            component.setDevMode("proCode");
            component.setFramework(bundleDto.getFramework());
            component.setPublicStatus(1);
            component.setIsTinyReserved(false);
            if (snippets == null || snippets.isEmpty()) {
                componentList.add(component);
                continue;
            }
            for (Child child : snippets) {

                Snippet snippet = child.getChildren().stream()
                        .filter(item -> toPascalCase(comp.get("component").toString())
                                .equals(toPascalCase(item.getSnippetName())))
                        .findFirst()
                        .orElse(null);

                if (snippet != null) {
                    Map<String, Object> snippetMap = BeanUtil.beanToMap(snippet);
                    component.setSnippets(Arrays.asList(snippetMap));

                    component.setCategory(child.getGroup());
                }
            }
            componentList.add(component);
        }

        return bulkCreate(componentList);
    }

    private Result<FileResult> bulkCreate(List<Component> componentList) {
        int addNum = 0;
        int updateNum = 0;
        for (Component component : componentList) {
            // 构建查询条件，假设 key 作为唯一键
            // 查询数据库中是否存在该记录
            Component componentParam = new Component();
            componentParam.setComponent(component.getComponent());
            componentParam.setName(component.getName());
            List<Component> queryComponent = findComponentByCondition(componentParam);

            if (queryComponent.isEmpty()) {
                // 插入新记录
                Integer result = createComponent(component);
                if (result == 1) {
                    MaterialComponent materialComponent = new MaterialComponent();
                    materialComponent.setMaterialId(1);
                    materialComponent.setComponentId(component.getId());
                    componentMapper.createMaterialComponent(materialComponent);
                    MaterialHistoryComponent materialHistoryComponent = new MaterialHistoryComponent();
                    materialHistoryComponent.setComponentId(component.getId());
                    materialHistoryComponent.setMaterialHistoryId(1);
                    componentMapper.createMaterialHistoryComponent(materialHistoryComponent);
                }
                addNum = addNum + 1;
            } else {
                // 更新记录
                component.setId(queryComponent.get(0).getId());
                updateComponentById(component);
                updateNum = updateNum + 1;
            }
        }

        // 构造返回插入和更新的条数
        FileResult fileResult = new FileResult();
        fileResult.setInsertNum(addNum);
        fileResult.setUpdateNum(updateNum);
        return Result.success(fileResult);
    }

    private String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean isNextUpper = true;
        for (char c : input.toCharArray()) {
            if (isNextUpper) {
                result.append(Character.toUpperCase(c));
                isNextUpper = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
}
