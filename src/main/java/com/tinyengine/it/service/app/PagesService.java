package com.tinyengine.it.service.app;

import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.Pages;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PagesService {


    /**
     * 根据主键id查询表pages信息
     *
     * @param id
     */
    PageDto findPagesById(@Param("id") Integer id);

    /**
     * 根据条件查询表pages信息
     *
     * @param pages
     */
    List<PageDto> findPagesByCondition(Pages pages);


    /**
     * 创建页面
     *
     * @param pages
     */
    PageDto createPage(Pages pages);

    /**
     * 创建文件夹
     *
     * @param pages
     */
    PageDto createFolder(Pages pages);

    /**
     * 根据主键id删除pages数据
     *
     * @param id
     */
    Result<PageDto> delPage(@Param("id") Integer id) throws Exception;

    /**
     * 根据页面id去获取页面的区块、appshema等的页面信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    PageDto getPageById(Integer id) throws Exception;

    /**
     * 更新页面
     *
     * @param pages
     * @return
     * @throws Exception
     */
    Result<PageDto> updatePage(Pages pages) throws Exception;

    /**
     * 更新页面文件夹
     *
     * @param pages
     * @return
     */
    Result<PageDto> update(Pages pages) throws Exception;

    /**
     * 查询页面schemaCode
     *
     * @param schemaCodeParam
     * @return schemaCode
     */
    List<Map<String, Object>> schema2Code(SchemaCodeParam schemaCodeParam);

    /**
     * 查询页面预览元数据
     *
     * @param previewParam
     * @return PreviewDto
     */
    PreviewDto getPreviewMetaData(PreviewParam previewParam);


    /**
     * 查询页面或区块源码
     *
     * @param dslCodeParam
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> dslCode(DslCodeParam dslCodeParam);
}
