package com.tinyengine.it.service.app.v1;

import com.tinyengine.it.exception.ServiceException;

import java.util.Map;

public interface AppV1Service {
    /**
     * 查询app schema
     *
     * @param id
     */
    Map<String, Object> appSchema(Integer id) throws ServiceException;

    Map<String, Map<String, String>> mergeEntries(Map<String, Map<String, String>> appEntries, Map<String, Map<String, String>> blockEntries);

}
