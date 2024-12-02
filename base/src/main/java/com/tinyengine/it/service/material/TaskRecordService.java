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

import com.tinyengine.it.model.entity.TaskRecord;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务记录service
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
public interface TaskRecordService {
    /**
     * 根据主键id查询表task_record信息
     *
     * @param id id
     * @return the task record
     */
    TaskRecord queryTaskRecordById(@Param("id") Integer id);

    /**
     * 获取任务状态
     *
     * @param taskTypeId the task type id
     * @param uniqueIds  the unique ids
     * @return task record
     */
    List<List<TaskRecord>> status(int taskTypeId, String uniqueIds);
}
