package com.tinyengine.it.service.impl.material;


import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ServiceException;
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
     * @param id
     */
    @Override
    public TaskRecord findTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.findTaskRecordById(id);
    }


    /**
     * 新增表task_record数据
     *
     * @param taskRecord
     */
    @Override
    public Integer createTaskRecord(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.createTaskRecord(taskRecord);
    }

    /**
     * 获取任务状态
     *
     * @param taskTypeId
     * @param uniqueIds
     * @return
     */
    @SystemServiceLog(description = "status 获取任务状态 实现类")
    @Override
    public List<List<TaskRecord>> status(int taskTypeId, String uniqueIds) {
        String[] idArray = uniqueIds.split(",");
        List<Integer> uniqueIdsList = Arrays.stream(idArray).map(Integer::parseInt).collect(Collectors.toList());

        List<CompletableFuture<List<TaskRecord>>> queryPromises = uniqueIdsList.stream()
                .map(uniqueId -> CompletableFuture.supplyAsync(() -> {
                    // 根据taskTypeId、uniqueId、created_at按照条件查询
                    return taskRecordMapper.fingTaskRecordByTaskIdAndUniqueid(taskTypeId, uniqueId);
                }))
                .collect(Collectors.toList());
        return queryPromises.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    /**
     * 获取未完成任务状态
     *
     * @param taskTypeId
     * @param uniqueIds
     * @return
     */
    @Override
    public TaskRecord getUnfinishedTask(int taskTypeId, int uniqueIds) {
        return taskRecordMapper.getUnfinishedTask(taskTypeId, uniqueIds);
    }
}
