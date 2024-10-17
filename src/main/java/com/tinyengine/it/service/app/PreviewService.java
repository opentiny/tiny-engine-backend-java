package com.tinyengine.it.service.app;

import java.util.Map;

public interface PreviewService {
    Map<String,String> start(Integer id);
    String generateAndUpload(Apps appComplexInfo);

}
