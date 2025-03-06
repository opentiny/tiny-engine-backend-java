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

import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.AiParam;
import com.tinyengine.it.service.app.AiChatService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AiChatControllerTest {
    @Mock
    private AiChatService aiChatService;
    @InjectMocks
    private AiChatController aiChatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAiChat() {
        AiParam aiParam = new AiParam();
        HashMap<String, Object> mockData = new HashMap<String, Object>() {
            {
                put("key", "parameter");
            }
        };
        Result<Map<String, Object>> mapResult = Result.success(mockData);

        when(aiChatService.getAnswerFromAi(aiParam)).thenReturn(mapResult);

        Result<Map<String, Object>> result = aiChatController.aiChat(aiParam);
        Assertions.assertEquals("parameter", result.getData().get("key"));
    }
}

