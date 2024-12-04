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
    private AppV1Service appV1Service;
    @InjectMocks
    private AppV1Controller appV1Controller;

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

