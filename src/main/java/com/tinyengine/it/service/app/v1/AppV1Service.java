package com.tinyengine.it.service.app.v1;

import com.tinyengine.it.common.exception.ServiceException;

import java.util.Map;

/**
 * The interface App v 1 service.
 */
public interface AppV1Service {
    /**
     * 查询app schema
     *
     * @param id the id
     * @return the map
     * @throws ServiceException the service exception
     */
    Map<String, Object> appSchema(Integer id) throws ServiceException;

    /**
     * Merge entries map.
     *
     * @param appEntries   the app entries
     * @param blockEntries the block entries
     * @return the map
     */
    Map<String, Map<String, String>> mergeEntries(Map<String, Map<String, String>> appEntries,
        Map<String, Map<String, String>> blockEntries);

}
