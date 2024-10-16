package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.TaskRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskRecordMapper extends BaseMapper<TaskRecord> {

    /**
     * 查询表task_record所有信息
     */
    List<TaskRecord> findAllTaskRecord();

    /**
     * 根据主键id查询表task_record数据
     *
     * @param id
     */
    TaskRecord findTaskRecordById(@Param("id") Integer id);

    /**
     * 根据条件查询表task_record数据
     *
     * @param taskRecord
     */
    List<TaskRecord> findTaskRecordByCondition(TaskRecord taskRecord);

    /**
     * 根据主键id删除表task_record数据
     *
     * @param id
     */
    Integer deleteTaskRecordById(@Param("id") Integer id);

    /**
     * 根据主键id更新表task_record数据
     *
     * @param taskRecord
     */
    Integer updateTaskRecordById(TaskRecord taskRecord);

    /**
     * 新增表task_record数据
     *
     * @param taskRecord
     */
    Integer createTaskRecord(TaskRecord taskRecord);

    /**
     * 根据taskTypeId、uniqueId按照created_at降序查询表task_record信息
     *
     * @param taskTypeId
     * @param uniqueId
     * @return
     */
    List<TaskRecord> fingTaskRecordByTaskIdAndUniqueid(@Param("taskTypeId") Integer taskTypeId, @Param("uniqueId") Integer uniqueId);

    /**
     * 根据taskTypeId、uniqueId按照id降序查询表task_record一条信息
     *
     * @param taskTypeId
     * @param uniqueId
     * @return
     */
    TaskRecord getUnfinishedTask(@Param("taskTypeId") Integer taskTypeId, @Param("uniqueId") Integer uniqueId);
}