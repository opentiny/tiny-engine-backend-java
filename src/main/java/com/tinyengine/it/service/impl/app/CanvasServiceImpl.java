package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.config.Enums;
import com.tinyengine.it.model.dto.PageDto;
import com.tinyengine.it.service.app.CanvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CanvasServiceImpl implements CanvasService {
    @Autowired
    PagesMapper pagesMapper;
    @Autowired
    BlocksMapper blocksMapper;
    @Autowired
    UsersPermissionsUserMapper usersPermissionsUserMapper;

    @Override
    public Map<String, Object> lockCanvas(Integer id, String state, String type) {
        int occupier;
        // TODO 先试用mock数据，后续添加登录及权限后从session获取,
        UsersPermissionsUser user = usersPermissionsUserMapper.findUsersPermissionsUserById(86);
        Map<String, Object> result = new HashMap<>();
        if ("page".equals(type)) {
            PageDto page = pagesMapper.findPagesById(id);
            occupier = page.getOccupier().getId();
            Boolean iCaDoIt = iCanDoIt(occupier, user);
            if (iCaDoIt) {
                int arg = state == Enums.E_CanvasEditorState.Occupy.getValue() ? user.getId() : null;
                Pages updatePage = new Pages();
                updatePage.setId(id);
                updatePage.setOccupier(arg);
                pagesMapper.updatePagesById(updatePage);
                result.put("operate", "success");
                result.put("occupier", user);
                return result;
            }
        } else {
            Blocks block = blocksMapper.findBlocksById(id);
            occupier = block.getOccupier();
            Boolean iCaDoIt = iCanDoIt(occupier, user);
            if (iCaDoIt) {
                int arg = state == Enums.E_CanvasEditorState.Occupy.getValue() ? user.getId() : null;
                Blocks updateBlock = new Blocks();
                updateBlock.setId(id);
                updateBlock.setOccupier(arg);
                blocksMapper.updateBlocksById(updateBlock);
                result.put("operate", "success");
                result.put("occupier", user);
                return result;
            }
        }
        result.put("operate", "failed");
        result.put("occupier", user);
        return result;
    }

    public Boolean iCanDoIt(Integer occupier, UsersPermissionsUser user) {
        if (occupier == user.getId()) {
            return true;
        }
        return false;
    }
}
