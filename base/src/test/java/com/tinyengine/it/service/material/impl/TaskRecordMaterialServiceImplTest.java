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

/**
 * test case
 *
 * @since 2024-11-19
 */
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
