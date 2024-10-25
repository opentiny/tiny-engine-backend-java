package com.tinyengine.it.controller;


import com.tinyengine.it.common.base.Result;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class HomeController {

    /**
     *  健康检测
     */
    @GetMapping("/healthCheck")
    public Result healthCheck() {
        return Result.success();
    }
}
