package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.TaskRecord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TaskRecordMapper extends BaseMapper<TaskRecord> {

    /**
     * 查询表t_task_record所有信息
     */
    List<TaskRecord> findAllTaskRecord();

    /**
     * 根据主键id查询表t_task_record数据
     *
     * @param id
     */
    TaskRecord findTaskRecordById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_task_record数据
     *
     * @param taskRecord
     */
    List<TaskRecord> findTaskRecordByCondition(TaskRecord taskRecord);

    /**
     * 根据主键id删除表t_task_record数据
     *
     * @param id
     */
    Integer deleteTaskRecordById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_task_record数据
     *
     * @param taskRecord
     */
    Integer updateTaskRecordById(TaskRecord taskRecord);

    /**
     * 新增表t_task_record数据
     *
     * @param taskRecord
     */
    Integer createTaskRecord(TaskRecord taskRecord);
}