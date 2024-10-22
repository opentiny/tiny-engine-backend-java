package com.tinyengine.it.service.app;

import com.tinyengine.it.model.dto.AiParam;
import com.tinyengine.it.model.dto.Result;

import java.util.Map;

public interface AiChatService {
    Result<Map<String, Object>> getAnswerFromAi(AiParam aiParam);
}
