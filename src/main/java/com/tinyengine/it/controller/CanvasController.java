
package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.CanvasDto;
import com.tinyengine.it.service.app.CanvasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * canvas api
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class CanvasController {
    @Autowired
    private CanvasService canvasService;

    /**
     * 区块及页面锁
     *
     * @param id the id
     * @param state the state
     * @param type the type
     * @return CanvasDto
     */
    @GetMapping("/canvas/lock")
    public Result<CanvasDto> lock(@PathVariable Integer id, String state, String type) {
        return canvasService.lockCanvas(id, state, type);
    }
}
