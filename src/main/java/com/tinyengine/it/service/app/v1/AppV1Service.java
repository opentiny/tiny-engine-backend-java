
package com.tinyengine.it.service.app.v1;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.model.dto.SchemaDto;
import com.tinyengine.it.model.dto.SchemaI18n;

import java.util.Map;

/**
 * The interface App v 1 service.
 *
 * @since 2024-10-20
 */
public interface AppV1Service {
    /**
     * 查询app schema
     *
     * @param id the id
     * @return the map
     * @throws ServiceException the service exception
     */
    SchemaDto appSchema(Integer id) throws ServiceException;

    /**
     * Merge entries map.
     *
     * @param appEntries the app entries
     * @param blockEntries the block entries
     * @return the map
     */
    SchemaI18n mergeEntries(SchemaI18n appEntries, SchemaI18n blockEntries);

}
