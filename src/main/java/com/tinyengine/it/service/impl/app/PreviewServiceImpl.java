package com.tinyengine.it.service.impl.app;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.tinyengine.it.model.dto.AppComplexInfoDto;
import com.tinyengine.it.model.entity.Apps;
import com.tinyengine.it.service.app.AppsService;
import com.tinyengine.it.service.app.PreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PreviewServiceImpl implements PreviewService {
    @Autowired
    AppsService appsService;

    @Override
    public Map<String, String> start(Integer id) {
        // 暂时不考虑 应用已有版本的应用出码
        AppComplexInfoDto appComplexInfo = appsService.calculateHashValue(id);
        String obsUrl = appComplexInfo.getAppInfo().getObsUrl();
        if (!appComplexInfo.getIsChanged() && !ObjectUtils.isEmpty(obsUrl)) {
            // Task latesTask = taskService.getLatestTask(Enums.E_TaskType.APP_BUILD.getValue(), appId);
            // if(ObjectUtils.isNotEmpty(latesTask)){

            //}
        }
        return null;
    }

    @Override
    public String generateAndUpload(Apps appComplexInfo) {
        return null;
    }
}
