package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.exception.ServiceException;
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
 */
@Service
@Slf4j
public class TaskRecordServiceImpl implements TaskRecordService {

    @Autowired
    private TaskRecordMapper taskRecordMapper;

    /**
     * 查询表t_task_record所有数据
     */
    @Override
    public List<TaskRecord> queryAllTaskRecord() throws ServiceException {
        return taskRecordMapper.queryAllTaskRecord();
    }

    /**
     * 根据主键id查询表t_task_record信息
     *
     * @param id
     */
    @Override
    public TaskRecord queryTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.queryTaskRecordById(id);
    }

    /**
     * 根据条件查询表t_task_record数据
     *
     * @param taskRecord
     */
    @Override
    public List<TaskRecord> queryTaskRecordByCondition(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.queryTaskRecordByCondition(taskRecord);
    }

    /**
     * 根据主键id删除表t_task_record数据
     *
     * @param id
     */
    @Override
    public Integer deleteTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.deleteTaskRecordById(id);
    }

    /**
     * 根据主键id更新表t_task_record数据
     *
     * @param taskRecord
     */
    @Override
    public Integer updateTaskRecordById(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.updateTaskRecordById(taskRecord);
    }

    /**
     * 新增表t_task_record数据
     *
     * @param taskRecord
     */
    @Override
    public Integer createTaskRecord(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.createTaskRecord(taskRecord);
    }
}
