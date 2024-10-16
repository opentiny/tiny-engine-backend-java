package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.TaskRecordMapper;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.app.TaskRecordService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TaskRecordServiceImpl implements TaskRecordService {

    @Autowired
    TaskRecordMapper taskRecordMapper;


    /**
     * 根据主键id查询表task_record信息
     *
     * @param id
     */
    @SystemServiceLog(description = "findTaskRecordById 根据主键id查询表task_record信息 实现类")
    @Override
    public TaskRecord findTaskRecordById(@Param("id") Integer id) throws ServiceException {
        return taskRecordMapper.findTaskRecordById(id);
    }
}
