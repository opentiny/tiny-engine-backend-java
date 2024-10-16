package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.TaskRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskRecordService {


    /**
     * 根据主键id查询表task_record信息
     *
     * @param id
     */
    TaskRecord findTaskRecordById(@Param("id") Integer id);


}
