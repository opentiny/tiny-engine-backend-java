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

@Service
public class TaskRecordMaterialServiceImpl implements TaskRecordService {
    @Autowired
    TaskRecordMapper taskRecordMapper;


    /**
     * 根据主键id查询表task_record信息
     *
     * @param id id
     */
    @Override
    public TaskRecord queryTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.queryTaskRecordById(id);
    }


    /**
     * 新增表task_record数据
     *
     * @param taskRecord the task record
     */
    @Override
    public Integer createTaskRecord(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.createTaskRecord(taskRecord);
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

    /**
     * 获取未完成任务状态
     *
     * @param taskTypeId the task type id
     * @param uniqueIds  the unique ids
     * @return task record
     */
    @Override
    public TaskRecord getUnfinishedTask(int taskTypeId, int uniqueIds) {
        return taskRecordMapper.getUnfinishedTask(taskTypeId, uniqueIds);
    }
}
