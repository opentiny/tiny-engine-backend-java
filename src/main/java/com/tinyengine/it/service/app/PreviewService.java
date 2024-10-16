package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.Apps;

import java.util.Map;

public interface PreviewService {
    Map<String,String> start(Integer id);
    String generateAndUpload(Apps appComplexInfo);

}
