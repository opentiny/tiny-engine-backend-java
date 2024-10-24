package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.AiParam;

import java.util.Map;

/**
 * The interface Ai chat service.
 */
public interface AiChatService {
    /**
     * Gets answer from ai.
     *
     * @param aiParam the ai param
     * @return the answer from ai
     */
    Result<Map<String, Object>> getAnswerFromAi(AiParam aiParam);
}
