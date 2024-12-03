/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.CanvasDto;
import com.tinyengine.it.service.app.CanvasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * canvas api
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@RequestMapping("/app-center/api")
public class CanvasController {
    @Autowired
    private CanvasService canvasService;

    /**
     * 区块及页面锁
     *
     * @param id    the id
     * @param state the state
     * @param type  the type
     * @return CanvasDto
     */
    @GetMapping("apps/canvas/lock")
    public Result<CanvasDto> lock(@RequestParam Integer id, @RequestParam String state, @RequestParam String type) {
        return canvasService.lockCanvas(id, state, type);
    }
}
