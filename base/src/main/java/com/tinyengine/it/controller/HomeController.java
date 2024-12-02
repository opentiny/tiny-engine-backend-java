package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * home api
 *
 * @since 2024-10-26
 */
@RestController
@RequestMapping("/api")
public class HomeController {
    /**
     * 健康检测
     *
     * @return Result
     */
    @GetMapping("/healthCheck")
    public Result<Boolean> healthCheck() {
        return Result.success(true);
    }
}
