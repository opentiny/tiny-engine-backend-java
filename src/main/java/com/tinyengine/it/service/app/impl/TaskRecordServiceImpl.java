
package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.mapper.TaskRecordMapper;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.app.TaskRecordService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Task record service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class TaskRecordServiceImpl implements TaskRecordService {
    @Autowired
    private TaskRecordMapper taskRecordMapper;

    /**
     * 查询表t_task_record所有数据
     *
     * @return TaskRecord
     */
    @Override
    public List<TaskRecord> queryAllTaskRecord() {
        return taskRecordMapper.queryAllTaskRecord();
    }

    /**
     * 根据主键id查询表t_task_record信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public TaskRecord queryTaskRecordById(@Param("id") Integer id) {
        return taskRecordMapper.queryTaskRecordById(id);
    }

    /**
     * 根据条件查询表t_task_record数据
     *
     * @param taskRecord taskRecord
     * @return query result
     */
    @Override
    public List<TaskRecord> queryTaskRecordByCondition(TaskRecord taskRecord) {
        return taskRecordMapper.queryTaskRecordByCondition(taskRecord);
    }

    /**
     * 根据主键id删除表t_task_record数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteTaskRecordById(@Param("id") Integer id) {
        return taskRecordMapper.deleteTaskRecordById(id);
    }

    /**
     * 根据主键id更新表t_task_record数据
     *
     * @param taskRecord taskRecord
     * @return execute success data number
     */
    @Override
    public Integer updateTaskRecordById(TaskRecord taskRecord) {
        return taskRecordMapper.updateTaskRecordById(taskRecord);
    }

    /**
     * 新增表t_task_record数据
     *
     * @param taskRecord taskRecord
     * @return execute success data number
     */
    @Override
    public Integer createTaskRecord(TaskRecord taskRecord) {
        return taskRecordMapper.createTaskRecord(taskRecord);
    }
}
