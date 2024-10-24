package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.PreviewParam;
import com.tinyengine.it.model.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Page service.
 * @since 2024-10-20
 */
public interface PageService {

    /**
     * 查询表t_page所有信息
     *
     * @param aid the aid
     * @return the list
     */
    List<Page> queryAllPage(Integer aid);

    /**
     * 根据主键id查询表t_page信息
     *
     * @param id the id
     * @return the page
     * @throws Exception the exception
     */
    Page queryPageById(@Param("id") Integer id) throws Exception;

    /**
     * 根据条件查询表t_page信息
     *
     * @param page the page
     * @return the list
     */
    List<Page> queryPageByCondition(Page page);

    /**
     * 根据主键id删除pages数据
     *
     * @param id the id
     * @return the result
     * @throws Exception the exception
     */
    Result<Page> delPage(@Param("id") Integer id) throws Exception;

    /**
     * 创建页面
     *
     * @param page the page
     * @return the result
     * @throws Exception the exception
     */
    Result<Page> createPage(Page page) throws Exception;

    /**
     * 创建文件夹
     *
     * @param page the page
     * @return the result
     * @throws Exception the exception
     */
    Result<Page> createFolder(Page page) throws Exception;

    /**
     * 更新页面
     *
     * @param pages the pages
     * @return result
     * @throws Exception the exception
     */
    Result<Page> updatePage(Page pages) throws Exception;

    /**
     * 更新页面文件夹
     *
     * @param pages the pages
     * @return result
     * @throws Exception the exception
     */
    Result<Page> update(Page pages) throws Exception;

    /**
     * 查询页面预览元数据
     *
     * @param previewParam the preview param
     * @return PreviewDto preview meta data
     */
    PreviewDto getPreviewMetaData(PreviewParam previewParam);

}
