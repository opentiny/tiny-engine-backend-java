package com.tinyengine.it.service.app;

import java.util.Map;

public interface CanvasService {
    Map<String, Object> lockCanvas(Integer id, String state, String type);
}
