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

package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * AiMessages
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class AiMessages {
    /**
     * Message content - can be either:
     * - String: for simple text messages
     * - List: for multimodal content (text + images)
     */
    private Object content;
    private String role;
    private String name;

    /**
     * Get content as String (for backward compatibility)
     * If content is not a String, returns null
     *
     * @return content as String or null
     */
    public String getContentAsString() {
        if (content instanceof String) {
            return (String) content;
        }
        return null;
    }

    /**
     * Set content from String (for backward compatibility)
     *
     * @param content the content string
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Set content from Object (for multimodal support)
     *
     * @param content the content object
     */
    public void setContent(Object content) {
        this.content = content;
    }
}
