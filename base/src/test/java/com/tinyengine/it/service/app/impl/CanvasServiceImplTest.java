package com.tinyengine.it.service.app.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.dto.CanvasDto;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * test case
 *
 * @since 2024-10-29
 */
class CanvasServiceImplTest {
    @Mock
    private PageMapper pageMapper;
    @Mock
    private BlockMapper blockMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private CanvasServiceImpl canvasServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLockCanvasTypePage() {
        int pageId = 123;
        int userId = 123;
        Page page = new Page();
        User occupier = new User();
        occupier.setId(userId);
        page.setOccupier(occupier);

        when(pageMapper.queryPageById(pageId)).thenReturn(page);
        User user = new User();
        user.setId(userId);
        when(userMapper.queryUserById(1)).thenReturn(user);

        Result<CanvasDto> result = canvasServiceImpl.lockCanvas(pageId, "occupy", "page");

        verify(pageMapper, times(1)).updatePageById(any());
        Assertions.assertEquals("success", result.getData().getOperate());
        Assertions.assertEquals(userId, result.getData().getOccupier().getId());
    }

    @Test
    void testLockCanvasTypeIsNotPage() {
        int pageId = 123;
        int userId = 123;
        Block block = new Block();
        block.setOccupierBy(String.valueOf(userId));

        when(blockMapper.queryBlockById(anyInt())).thenReturn(block);
        User user = new User();
        user.setId(userId);
        when(userMapper.queryUserById(1)).thenReturn(user);

        Result<CanvasDto> result = canvasServiceImpl.lockCanvas(pageId, "occupy", "other");

        verify(blockMapper, times(1)).updateBlockById(any());
        Assertions.assertEquals("success", result.getData().getOperate());
        Assertions.assertEquals(userId, result.getData().getOccupier().getId());
    }
}
