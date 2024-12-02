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

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.TaskRecordMapper;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.material.TaskRecordService;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 物料模块任务记录实现类
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Service
public class TaskRecordMaterialServiceImpl implements TaskRecordService {
    @Autowired
    TaskRecordMapper taskRecordMapper;

    /**
     * 根据主键id查询表task_record信息
     *
     * @param id id
     * @return the task record
     * @throws ServiceException serviceException
     */
    @Override
    public TaskRecord queryTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.queryTaskRecordById(id);
    }

    /**
     * 获取任务状态
     *
     * @param taskTypeId the task type id
     * @param uniqueIds  the unique ids
     * @return task record
     */
    @SystemServiceLog(description = "status 获取任务状态 实现类")
    @Override
    public List<List<TaskRecord>> status(int taskTypeId, String uniqueIds) {
        String[] idArray = uniqueIds.split(",");
        List<Integer> uniqueIdsList = Arrays.stream(idArray).map(Integer::parseInt).collect(Collectors.toList());

        List<CompletableFuture<List<TaskRecord>>> queryPromises = uniqueIdsList.stream()
                .map(uniqueId -> CompletableFuture.supplyAsync(() -> {
                    // 根据taskTypeId、uniqueId、created_at按照条件查询
                    return taskRecordMapper.findTaskRecordByTaskIdAndUniqueid(taskTypeId, uniqueId);
                }))
                .collect(Collectors.toList());
        return queryPromises.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
