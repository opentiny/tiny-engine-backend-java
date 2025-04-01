/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.common.utils;

import java.lang.reflect.Field;

/**
 * 测试工工具类
 */
public class TestUtil {
    /**
     * 设置私有字段的属性值
     *
     * @param obj   对象
     * @param field 字段名
     * @param value 值
     * @throws NoSuchFieldException   异常
     * @throws IllegalAccessException 异常
     */
    public static void setPrivateValue(Object obj, String field, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = obj.getClass().getDeclaredField(field);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
    }
}
