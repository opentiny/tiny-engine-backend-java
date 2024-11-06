package com.tinyengine.it.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.CanvasDto;
import com.tinyengine.it.service.app.CanvasService;

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
class CanvasControllerTest {
    @Mock
    CanvasService canvasService;
    @InjectMocks
    CanvasController canvasController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLock() {
        when(canvasService.lockCanvas(anyInt(), anyString(), anyString())).thenReturn(new Result<CanvasDto>());

        Result<CanvasDto> result = canvasController.lock(Integer.valueOf(0), "state", "type");
        Assertions.assertEquals(new Result<CanvasDto>(), result);
    }
}
