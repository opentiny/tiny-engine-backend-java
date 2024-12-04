/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.app.impl;

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

/**
 * test case
 *
 * @since 2024-10-29
 */
class TaskRecordServiceImplTest {
    @Mock
    private TaskRecordMapper taskRecordMapper;
    @InjectMocks
    private TaskRecordServiceImpl taskRecordServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllTaskRecord() {
        TaskRecord mockData = new TaskRecord();
        when(taskRecordMapper.queryAllTaskRecord()).thenReturn(Arrays.<TaskRecord>asList(mockData));

        List<TaskRecord> result = taskRecordServiceImpl.queryAllTaskRecord();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testQueryTaskRecordById() {
        TaskRecord mockData = new TaskRecord();
        when(taskRecordMapper.queryTaskRecordById(1)).thenReturn(mockData);

        TaskRecord result = taskRecordServiceImpl.queryTaskRecordById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryTaskRecordByCondition() {
        TaskRecord mockData = new TaskRecord();
        TaskRecord param = new TaskRecord();
        when(taskRecordMapper.queryTaskRecordByCondition(param)).thenReturn(Arrays.asList(mockData));

        List<TaskRecord> result = taskRecordServiceImpl.queryTaskRecordByCondition(param);
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testDeleteTaskRecordById() {
        when(taskRecordMapper.deleteTaskRecordById(1)).thenReturn(2);

        Integer result = taskRecordServiceImpl.deleteTaskRecordById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateTaskRecordById() {
        TaskRecord param = new TaskRecord();
        when(taskRecordMapper.updateTaskRecordById(param)).thenReturn(2);

        Integer result = taskRecordServiceImpl.updateTaskRecordById(param);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testCreateTaskRecord() {
        TaskRecord param = new TaskRecord();
        when(taskRecordMapper.createTaskRecord(param)).thenReturn(2);

        Integer result = taskRecordServiceImpl.createTaskRecord(param);
        Assertions.assertEquals(2, result);
    }
}
