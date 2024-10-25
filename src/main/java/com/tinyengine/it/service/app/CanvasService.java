
package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.CanvasDto;

/**
 * canvas service
 *
 * @since @since 2024-10-20
 */
public interface CanvasService {
    /**
     * lock canvas
     *
     * @param id    id
     * @param state state
     * @param type  type
     * @return CanvasDto
     */
    Result<CanvasDto> lockCanvas(Integer id, String state, String type);
}
