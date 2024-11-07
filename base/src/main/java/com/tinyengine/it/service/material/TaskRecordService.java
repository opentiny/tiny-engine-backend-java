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
