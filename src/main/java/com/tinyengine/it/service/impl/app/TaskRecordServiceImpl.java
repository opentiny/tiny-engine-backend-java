package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.mapper.TaskRecordMapper;
import com.tinyengine.it.service.app.TaskRecordService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class TaskRecordServiceImpl implements TaskRecordService {

    @Autowired
    private TaskRecordMapper taskRecordMapper;

    /**
    *  查询表t_task_record所有数据
    */
    @Override
    public List<TaskRecord> findAllTaskRecord() throws ServiceException {
        return taskRecordMapper.findAllTaskRecord();
    }

    /**
    *  根据主键id查询表t_task_record信息
    *  @param id
    */
    @Override
    public TaskRecord findTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.findTaskRecordById(id);
    }

    /**
    *  根据条件查询表t_task_record数据
    *  @param taskRecord
    */
    @Override
    public List<TaskRecord> findTaskRecordByCondition(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.findTaskRecordByCondition(taskRecord);
    }

    /**
    *  根据主键id删除表t_task_record数据
    *  @param id
    */
    @Override
    public Integer deleteTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.deleteTaskRecordById(id);
    }

    /**
    *  根据主键id更新表t_task_record数据
    *  @param taskRecord
    */
    @Override
    public Integer updateTaskRecordById(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.updateTaskRecordById(taskRecord);
    }

    /**
    *  新增表t_task_record数据
    *  @param taskRecord
    */
    @Override
    public Integer createTaskRecord(TaskRecord taskRecord) throws ServiceException {
        return taskRecordMapper.createTaskRecord(taskRecord);
    }
}
