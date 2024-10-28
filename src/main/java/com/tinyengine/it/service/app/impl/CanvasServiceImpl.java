
package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
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

    @Override
    public Result<CanvasDto> lockCanvas(Integer id, String state, String type) {
        int occupier;
        // needTODO 先试用mock数据，后续添加登录及权限后从session获取,
        User user = userMapper.queryUserById(1);
        CanvasDto canvasDto = new CanvasDto();
        if ("page".equals(type)) {
            Page page = pageMapper.queryPageById(id);
            occupier = page.getOccupier().getId();
            Boolean iCaDoIt = iCanDoIt(occupier, user);
            if (iCaDoIt) {
                int arg = state == Enums.CanvasEditorState.OCCUPY.getValue() ? user.getId() : null;
                Page updatePage = new Page();
                updatePage.setId(id);
                updatePage.setOccupierBy(String.valueOf(arg));
                pageMapper.updatePageById(updatePage);
                canvasDto.setOperate("success");
                canvasDto.setOccupier(user);
                return Result.success(canvasDto);
            }
        } else {
            Block block = blockMapper.queryBlockById(id);
            occupier = Integer.parseInt(block.getOccupierBy());
            Boolean iCaDoIt = iCanDoIt(occupier, user);
            if (iCaDoIt) {
                int arg = state == Enums.CanvasEditorState.OCCUPY.getValue() ? user.getId() : null;
                Block updateBlock = new Block();
                updateBlock.setId(id);
                updateBlock.setOccupierBy(String.valueOf(arg));
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

    private Boolean iCanDoIt(Integer occupier, User user) {
        if (occupier == user.getId()) {
            return true;
        }
        return false;
    }
}
