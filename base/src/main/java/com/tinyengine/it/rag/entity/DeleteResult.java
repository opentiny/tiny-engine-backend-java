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

package com.tinyengine.it.rag.entity;

import lombok.Data;

/**
 * Delete result
 */
@Data
public class DeleteResult {
    private int deletedCount;
    private int failedCount;
    private String target;

    public DeleteResult() {}

    public DeleteResult(int deletedCount, int failedCount, String target) {
        this.deletedCount = deletedCount;
        this.failedCount = failedCount;
        this.target = target;
    }
}
