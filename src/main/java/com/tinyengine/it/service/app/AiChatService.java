package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.AiParam;

import java.util.Map;

public interface AiChatService {
    Result<Map<String, Object>> getAnswerFromAi(AiParam aiParam);
}
