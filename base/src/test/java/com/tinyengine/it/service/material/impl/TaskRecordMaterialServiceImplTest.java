package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.TaskRecordMapper;
import com.tinyengine.it.model.entity.TaskRecord;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class TaskRecordMaterialServiceImplTest {
    @Mock
    TaskRecordMapper taskRecordMapper;
    @InjectMocks
    TaskRecordMaterialServiceImpl taskRecordMaterialServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryTaskRecordById() {
        TaskRecord taskRecord = new TaskRecord();
        when(taskRecordMapper.queryTaskRecordById(1)).thenReturn(taskRecord);

        TaskRecord result = taskRecordMaterialServiceImpl.queryTaskRecordById(1);
        Assertions.assertEquals(taskRecord, result);
    }

    @Test
    void testStatus() {
        List<TaskRecord> mockData = Arrays.<TaskRecord>asList(new TaskRecord());
        when(taskRecordMapper.findTaskRecordByTaskIdAndUniqueid(anyInt(), anyInt())).thenReturn(mockData);

        List<List<TaskRecord>> result = taskRecordMaterialServiceImpl.status(0, "1,2");
        Assertions.assertEquals(mockData, result.get(0));
    }
}
