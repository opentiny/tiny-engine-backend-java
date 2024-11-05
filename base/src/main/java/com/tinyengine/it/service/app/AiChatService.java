package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.AiParam;

import java.util.Map;

/**
 * The interface Ai chat service.
 *
 * @since 2024-10-20
 */
public interface AiChatService {
    /**
     * Gets answer from AI .
     *
     * @param aiParam the AI param
     * @return the answer from AI
     */
    Result<Map<String, Object>> getAnswerFromAi(AiParam aiParam);
}
