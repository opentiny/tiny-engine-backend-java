package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.CanvasDto;

import java.util.Map;

public interface CanvasService {
    Result<CanvasDto> lockCanvas(Integer id, String state, String type);
}
