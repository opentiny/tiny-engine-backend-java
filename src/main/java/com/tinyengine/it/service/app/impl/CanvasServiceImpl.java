package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.mapper.BlockMapper;

import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.CanvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CanvasServiceImpl implements CanvasService {
    @Autowired
    PageMapper pageMapper;
    @Autowired
    BlockMapper blockMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Map<String, Object> lockCanvas(Integer id, String state, String type) {
        int occupier;
        // TODO 先试用mock数据，后续添加登录及权限后从session获取,
        User user = userMapper.queryUserById(1);
        Map<String, Object> result = new HashMap<>();
        if ("page".equals(type)) {
            Page page = pageMapper.queryPageById(id);
            occupier = page.getOccupier().getId();
            Boolean iCaDoIt = iCanDoIt(occupier, user);
            if (iCaDoIt) {
                int arg = state == Enums.E_CanvasEditorState.Occupy.getValue() ? user.getId() : null;
                Page updatePage = new Page();
                updatePage.setId(id);
                updatePage.setOccupierBy(String.valueOf(arg));
                pageMapper.updatePageById(updatePage);
                result.put("operate", "success");
                result.put("occupier", user);
                return result;
            }
        } else {
            Block block = blockMapper.queryBlockById(id);
            occupier = Integer.parseInt(block.getOccupierBy());
            Boolean iCaDoIt = iCanDoIt(occupier, user);
            if (iCaDoIt) {
                int arg = state == Enums.E_CanvasEditorState.Occupy.getValue() ? user.getId() : null;
                Block updateBlock = new Block();
                updateBlock.setId(id);
                updateBlock.setOccupierBy(String.valueOf(arg));
                blockMapper.updateBlockById(updateBlock);
                result.put("operate", "success");
                result.put("occupier", user);
                return result;
            }
        }
        result.put("operate", "failed");
        result.put("occupier", user);
        return result;
    }

    public Boolean iCanDoIt(Integer occupier, User user) {
        if (occupier == user.getId()) {
            return true;
        }
        return false;
    }
}
