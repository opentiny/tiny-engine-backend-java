package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.base.BaseQuery;
import com.tinyengine.it.model.entity.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
public interface PageMapper extends BaseMapper<Page> {

    /**
     * 查询表pages所有信息
     */
    List<Page> findAllPages();

    /**
     * 根据主键id查询表pages数据
     *
     * @param baseQuery
     */
    Page queryPageById(BaseQuery baseQuery);

    /**
     * 根据条件查询表pages数据
     *
     * @param page
     */

    List<Page> queryPageByCondition(Page page);

    /**
     * 根据主键id删除表pages数据
     *
     * @param id
     */
    Integer deletePageById(@Param("id") Integer id);

    /**
     * 根据主键id更新表pages数据
     *
     * @param page
     */
    Integer updatePageById(Page page);

    /**
     * 新增表pages数据
     *
     * @param page
     */
    Integer createPage(Page page);

    /**
     * 通过app查pages
     *
     * @param app
     */
    @Results({
            @Result(column = "occupier", property = "occupierBy"),
            @Result(column = "occupier", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UserMapper.queryUserById"))

    })
    @Select("select * from t_page  <where>\n" +
            "                    <if test=\"app != null\">\n" +
            "                        AND app_id = #{app}\n" +
            "                    </if>\n" +
            "                    <if test=\"tenantId != null\">\n" +
            "                        AND tenant_id = #{tenantId}\n" +
            "                    </if>\n" +
            "                </where>")
    List<Page> queryPageByApp(Integer app);
}