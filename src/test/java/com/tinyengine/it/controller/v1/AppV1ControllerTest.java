package com.tinyengine.it.controller.v1;

import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.SchemaDto;
import com.tinyengine.it.service.app.v1.AppV1Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AppV1ControllerTest {
    @Mock
    AppV1Service appV1Service;
    @InjectMocks
    AppV1Controller appV1Controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSchema() {
        SchemaDto dto = new SchemaDto();
        dto.setVersion("version");
        when(appV1Service.appSchema(1)).thenReturn(dto);

        Result<SchemaDto> result = appV1Controller.getSchema(1);
        Assertions.assertEquals("version", result.getData().getVersion());
    }
}

