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

package com.tinyengine.it.common.utils;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * test case
 *
 * @since 2024-10-29
 */
class SchemaTest {
    @Mock
    private Set<String> groups;

    @InjectMocks
    private Schema schema;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssembleFieldsTypeApp() {
        Map<String, Object> result = schema.assembleFields(new HashMap<String, Object>() {
            {
                put("appData", "appData");
            }
        }, "app");
        Assertions.assertEquals(new HashMap<String, Object>() {
            {
                put("appData", "appData");
            }
        }, result);
    }

    @Test
    void testGetFolderSchema() {
        Map<String, Object> result = schema.getFolderSchema(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        });
        Assertions.assertEquals(new HashMap<String, Object>() {
            {
                put("data", "data");
                put("componentName", "Folder");
            }
        }, result);
    }

    @Test
    void testGetSchemaBase() {
        HashMap<String, Object> data = new HashMap<String, Object>() {
            {
                Map mapData = new HashMap();
                put("page_content", mapData);
                put("name", "name");
            }
        };
        Map<String, Object> result = schema.getSchemaBase(data);
        Assertions.assertEquals("name", result.get("fileName"));
    }

    @Test
    void testFilterFields() {
        Map<String, Object> result = schema.filterFields(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        }, Arrays.<String>asList("data"));
        Assertions.assertEquals(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        }, result);
    }

    @Test
    void testFormatFields() {
        Map<String, Object> result = schema.formatFields(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        }, new HashMap<String, String>() {
            {
                put("formatConfig", "formatConfig");
            }
        });
        Assertions.assertEquals(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        }, result);
    }

    @Test
    void testConvertFields() {
        Map<String, Object> result = schema.convertFields(new HashMap<String, Object>() {
            {
                put("data", "data");
                put("data2", "data2");
            }
        }, new HashMap<String, String>() {
            {
                put("data2", "data2");
            }
        });
        Assertions.assertEquals(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        }, result);
    }

    @Test
    void testToFormatString() {
        String result = schema.toFormatString("value");
        Assertions.assertEquals("value", result);
    }

    @Test
    void testToLocalTimestamp() {
        String result = schema.toLocalTimestamp("2024-10-26 00:00:00");
        Assertions.assertEquals("2024-10-26 00:00:00", result);
    }

    @Test
    void testToCreatorName() {
        String result = schema.toCreatorName("value");
        Assertions.assertEquals("Creator: value", result);
    }

    @Test
    void testToRootElement() {
        String result = schema.toRootElement(Boolean.TRUE);
        Assertions.assertEquals("body", result);
        result = schema.toRootElement(Boolean.FALSE);
        Assertions.assertEquals("div", result);
    }

    @Test
    void testToGroupName() {
        when(groups.contains(any(Object.class))).thenReturn(true);

        String result = schema.toGroupName("static");
        Assertions.assertEquals("staticPages", result);
    }
}

