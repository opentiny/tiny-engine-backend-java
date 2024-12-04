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

package com.tinyengine.it.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.material.TaskRecordService;

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
 * @since 2024-11-05
 */
class TaskRecordMaterialControllerTest {
    @Mock
    TaskRecordService taskRecordService;
    @InjectMocks
    TaskRecordMaterialController taskRecordMaterialController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTaskRecordById() {
        TaskRecord taskRecord = new TaskRecord();
        when(taskRecordService.queryTaskRecordById(anyInt())).thenReturn(taskRecord);

        Result<TaskRecord> result = taskRecordMaterialController.getTaskRecordById(Integer.valueOf(0));
        Assertions.assertEquals(taskRecord, result.getData());
    }

    @Test
    void testGetTasksStatus() {
        TaskRecord taskRecord = new TaskRecord();
        when(taskRecordService.status(anyInt(), anyString()))
                .thenReturn(Arrays.<List<TaskRecord>>asList(Arrays.<TaskRecord>asList(taskRecord)));

        Result<List<TaskRecord>> result = taskRecordMaterialController.getTasksStatus("1", "2");
        Assertions.assertEquals(taskRecord, result.getData().get(0));
    }
}
