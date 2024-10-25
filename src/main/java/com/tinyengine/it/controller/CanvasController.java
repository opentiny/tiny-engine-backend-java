package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.CanvasDto;
import com.tinyengine.it.service.app.CanvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class CanvasController {
    @Autowired
    CanvasService canvasService;

    /**
     * 区块及页面锁
     *
     * @param id the id
     * @param state the state
     * @param type the type
     * @return
     */
    @GetMapping("/canvas/lock")
    public Result<CanvasDto> lock(@PathVariable Integer id, String state, String type) {
        return canvasService.lockCanvas(id, state, type);
    }
}
