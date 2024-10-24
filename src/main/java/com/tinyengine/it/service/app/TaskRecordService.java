package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.TaskRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Task record service.
 * @since 2024-10-20
 */
public interface TaskRecordService {

    /**
     * 查询表t_task_record所有信息
     *
     * @return the list
     */
    List<TaskRecord> queryAllTaskRecord();

    /**
     * 根据主键id查询表t_task_record信息
     *
     * @param id the id
     * @return the task record
     */
    TaskRecord queryTaskRecordById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_task_record信息
     *
     * @param taskRecord the task record
     * @return the list
     */
    List<TaskRecord> queryTaskRecordByCondition(TaskRecord taskRecord);

    /**
     * 根据主键id删除t_task_record数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteTaskRecordById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_task_record信息
     *
     * @param taskRecord the task record
     * @return the integer
     */
    Integer updateTaskRecordById(TaskRecord taskRecord);

    /**
     * 新增表t_task_record数据
     *
     * @param taskRecord the task record
     * @return the integer
     */
    Integer createTaskRecord(TaskRecord taskRecord);
}
