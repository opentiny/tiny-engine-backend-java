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

package com.tinyengine.it.service.app.v1;

import com.tinyengine.it.model.dto.ChatRequest;

/**
 * The interface AIChat v 1 service.
 *
 * @since 2025-08-06
 */
public interface AiChatV1Service {
    /**
     * chatCompletion.
     *
     * @param request the request
     * @return Object the Object
     */
    public Object chatCompletion(ChatRequest request) throws Exception;

    /**
     * get token.
     *
     * @param apiKey the apiKey
     * @return token the token
     */
    public String getToken(String apiKey) throws Exception;
}
