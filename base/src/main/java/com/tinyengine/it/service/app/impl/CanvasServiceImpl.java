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

package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.dto.CanvasDto;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.CanvasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * canvas service
 *
 * @since 2024-10-20
 */
@Service
public class CanvasServiceImpl implements CanvasService {
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginUserContext loginUserContext;

    @Override
    public Result<CanvasDto> lockCanvas(Integer id, String state, String type) {
        String occupier;
        // needTODO 先试用mock数据，后续添加登录及权限后从session获取,
        User user = userMapper.queryUserById(loginUserContext.getLoginUserId());
        if (user == null) {
            user = new User();
            user.setId(loginUserContext.getLoginUserId());
        }
        CanvasDto canvasDto = new CanvasDto();
        if ("page".equals(type)) {
            Page page = pageMapper.queryPageById(id);
            occupier = page.getOccupier().getId();
            Boolean isCaDoIt = isCanDoIt(occupier, user);
            if (isCaDoIt) {
                Page updatePage = new Page();
                updatePage.setId(id);
                updatePage.setOccupierBy(user.getId());
                pageMapper.updatePageById(updatePage);
                canvasDto.setOperate("success");
                canvasDto.setOccupier(user);
                return Result.success(canvasDto);
            }
        } else {
            Block block = blockMapper.queryBlockById(id);
            occupier = block.getOccupierBy();
            Boolean isCaDoIt = isCanDoIt(occupier, user);
            if (isCaDoIt) {
                Block updateBlock = new Block();
                updateBlock.setId(id);
                updateBlock.setOccupierBy(user.getId());
                blockMapper.updateBlockById(updateBlock);
                canvasDto.setOperate("success");
                canvasDto.setOccupier(user);
                return Result.success(canvasDto);
            }
        }
        canvasDto.setOperate("failed");
        canvasDto.setOccupier(user);
        return Result.success(canvasDto);
    }

    private Boolean isCanDoIt(String occupier, User user) {
        return occupier == null || occupier.equals(user.getId());
    }
}
