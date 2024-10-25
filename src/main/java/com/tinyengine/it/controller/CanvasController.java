package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
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
     * @param id, state, type
     * @return
     */
    @GetMapping("/canvas/lock")
    public Result<Map<String, Object>> lock(@PathVariable Integer id, String state, String type) {
        Map<String, Object> result = canvasService.lockCanvas(id, state, type);
        return Result.success(result);
    }
}
