package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.AppsDto;
import com.tinyengine.it.model.entity.Apps;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AppsMapper extends BaseMapper<Apps> {

    /**
     * 查询表apps所有信息
     */
    List<Apps> findAllApps();

    /**
     * 根据主键id查询表apps数据
     *
     * @param id
     */
    Apps findAppsById(@Param("id") Integer id);

    /**
     * 根据条件查询表apps数据
     *
     * @param apps
     */
    List<Apps> findAppsByCondition(Apps apps);

    /**
     * 根据主键id删除表apps数据
     *
     * @param id
     */
    Integer deleteAppsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表apps数据
     *
     * @param apps
     */
    Integer updateAppsById(Apps apps);

    /**
     * 新增表apps数据
     *
     * @param apps
     */
    Integer createApps(Apps apps);

    /**
     * 根据appId查询App以及关联的区块分组和区块分类信息
     *
     * @param id
     * @return
     */
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", javaType = List.class, property = "block_categories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockCategoriesMapper.findBlockCategoriesByAppId")),
            @Result(column = "id", javaType = List.class, property = "block_groups",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockGroupsMapper.findBlockGroupByAppId"))
    })
    @Select("select * from apps where id = #{id}")
    AppsDto findAppsAndBlockGroupAndBlockCateById(Integer id);


    /**
     * 查询所有的App以及关联的区块分组和区块分类信息
     *
     * @return
     */
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", javaType = List.class, property = "block_categories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockCategoriesMapper.findBlockCategoriesByAppId")),
            @Result(column = "id", javaType = List.class, property = "block_groups",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockGroupsMapper.findBlockGroupByAppId"))
    })
    @Select("select * from apps")
    List<AppsDto> findAllAppsAndBlockGroupAndBlockCate();
}