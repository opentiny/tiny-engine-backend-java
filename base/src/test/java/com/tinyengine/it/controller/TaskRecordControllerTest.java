package com.tinyengine.it.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.app.TaskRecordService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * test case
 *
 * @since 2024-11-05
 */
class TaskRecordControllerTest {
    @Mock
    TaskRecordService taskRecordService;
    @InjectMocks
    TaskRecordController taskRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTaskRecordById() {
        TaskRecord taskRecord = new TaskRecord();
        when(taskRecordService.queryTaskRecordById(anyInt())).thenReturn(taskRecord);

        Result<TaskRecord> result = taskRecordController.getTaskRecordById(Integer.valueOf(0));
        Assertions.assertEquals(taskRecord, result.getData());
    }
}
