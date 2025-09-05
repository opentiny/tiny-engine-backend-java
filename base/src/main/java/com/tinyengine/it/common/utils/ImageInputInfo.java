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

/**
 * 图片输入信息类
 */
public  class ImageInputInfo {
    private final String cleanBase64;
    private final String format;
    private final boolean isDataUri;
    private final String mimeType;

    public ImageInputInfo(String cleanBase64, String format, boolean isDataUri, String mimeType) {
        this.cleanBase64 = cleanBase64;
        this.format = format;
        this.isDataUri = isDataUri;
        this.mimeType = mimeType;
    }

    public String getCleanBase64() {
        return cleanBase64;
    }

    public String getFormat() {
        return format;
    }

    public boolean isDataUri() {
        return isDataUri;
    }

    public String getMimeType() {
        return mimeType;
    }
}