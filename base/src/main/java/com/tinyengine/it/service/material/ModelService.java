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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tinyengine.it.model.entity.Model;

import java.io.IOException;
import java.util.List;

public interface ModelService extends IService<Model>{
    /**
     * 查询表t_model信息
     *
     * @return the Model
     */
    Model queryModelById(Integer id);

    /**
     * 根据name查询表t_model信息
     *
     * @return the model list
     */
    List<Model> getModelByName(String nameCn);

    /**
     * 分页查询表t_model
     *
     * @return the Page
     */
    Page<Model> pageQuery(int currentPage, int pageSize, String nameCn, String nameEn);

    /**
     * 创建t_model
     *
     * @return the model
     * @ param the model
     */
    Model createModel(Model model);

    /**
     * 删除t_model
     *
     * @return the Model
     * @ param the id
     */
    Model deleteModelById(Integer id);

    /**
     * 修改t_model
     *
     * @return the model
     * @ param the model
     */
    Model updateModelById(Model model);

    /**
     * 获取Model建表sql
     *
     * @ param the id
     * @return the String
     * @throws IOException
     */
    String getTableById(Integer id);

    /**
     * 获取所有模型的建表SQL语句
     * @return 拼接好的SQL语句字符串，每个表的SQL用分号分隔并换行
     */
    String getAllTable();
}
