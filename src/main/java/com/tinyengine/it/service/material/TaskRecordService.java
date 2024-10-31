package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.TaskRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskRecordService {


    /**
     * 根据主键id查询表task_record信息
     *
     * @param id id
     */
    TaskRecord queryTaskRecordById(@Param("id") Integer id);


    /**
     * 新增表task_record数据
     *
     * @param taskRecord the task record
     */
    Integer createTaskRecord(TaskRecord taskRecord);

    /**
     * 获取任务状态
     *
     * @param taskTypeId the task type id
     * @param uniqueIds  the unique ids
     * @return task record
     */
    List<List<TaskRecord>> status(int taskTypeId, String uniqueIds);

    /**
     * 获取未完成任务
     *
     * @param taskTypeId the task type id
     * @param uniqueIds  the unique ids
     * @return task record
     */
    TaskRecord getUnfinishedTask(int taskTypeId, int uniqueIds);
}
