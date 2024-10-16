package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.PageDto;
import com.tinyengine.it.model.entity.Pages;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PagesMapper extends BaseMapper<Pages> {

    /**
     * 查询表pages所有信息
     */
    List<Pages> findAllPages();

    /**
     * 根据主键id查询表pages数据
     *
     * @param id
     */
    @Results({
            @Result(column = "occupier", property = "occupierId"),
            @Result(column = "occupier", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UsersPermissionsUserMapper.findUsersPermissionsUserById"))

    })
    @Select("select * from pages where id = #{id}")
    PageDto findPagesById(@Param("id") Integer id);

    /**
     * 根据条件查询表pages数据
     *
     * @param pages
     */

    List<PageDto> findPagesByCondition(Pages pages);

    /**
     * 根据主键id删除表pages数据
     *
     * @param id
     */
    Integer deletePagesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表pages数据
     *
     * @param pages
     */
    Integer updatePagesById(Pages pages);

    /**
     * 新增表pages数据
     *
     * @param pages
     */
    Integer createPages(Pages pages);

    /**
     * 通过app查pages
     *
     * @param app
     */
    @Results({
            @Result(column = "occupier", property = "occupierId"),
            @Result(column = "occupier", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UsersPermissionsUserMapper.findUsersPermissionsUserById"))

    })
    @Select("select * from pages where app = #{app}")
    List<PageDto> findPagesByApp(Integer app);
}