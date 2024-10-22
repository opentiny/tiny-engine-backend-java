package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.PreviewParam;
import com.tinyengine.it.model.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageService {

    /**
     * 查询表t_page所有信息
     */
    List<Page> queryAllPage(Integer aid);

    /**
     * 根据主键id查询表t_page信息
     *
     * @param id
     */
    Page queryPageById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_page信息
     *
     * @param page
     */
    List<Page> queryPageByCondition(Page page);


    /**
     * 根据主键id删除pages数据
     *
     * @param id
     */
    Result<Page> delPage(@Param("id") Integer id) throws Exception;

    /**
     * 创建页面
     *
     * @param page
     */
    Result<Page> createPage(Page page);

    /**
     * 创建文件夹
     *
     * @param page
     */
    Result<Page> createFolder(Page page);

    /**
     * 更新页面
     *
     * @param pages
     * @return
     * @throws Exception
     */
    Result<Page> updatePage(Page pages) throws Exception;

    /**
     * 更新页面文件夹
     *
     * @param pages
     * @return
     */
    Result<Page> update(Page pages) throws Exception;


    /**
     * 查询页面预览元数据
     *
     * @param previewParam
     * @return PreviewDto
     */
    PreviewDto getPreviewMetaData(PreviewParam previewParam);


}
