package com.tinyengine.it.service.app.impl;

import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.entity.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class UserServiceImplTest {
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllUser() {
        User mockData = new User();
        when(userMapper.queryAllUser()).thenReturn(Arrays.<User>asList(mockData));

        List<User> result = userServiceImpl.queryAllUser();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testQueryUserById() {
        User mockData = new User();
        when(userMapper.queryUserById(1)).thenReturn(mockData);

        User result = userServiceImpl.queryUserById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryUserByCondition() {
        User mockData = new User();
        User param = new User();
        when(userMapper.queryUserByCondition(param)).thenReturn(Arrays.<User>asList(mockData));

        List<User> result = userServiceImpl.queryUserByCondition(param);
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testDeleteUserById() {
        when(userMapper.deleteUserById(1)).thenReturn(2);

        Integer result = userServiceImpl.deleteUserById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateUserById() {
        User param = new User();
        when(userMapper.updateUserById(param)).thenReturn(1);

        Integer result = userServiceImpl.updateUserById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateUser() {
        User param = new User();
        when(userMapper.createUser(param)).thenReturn(1);

        Integer result = userServiceImpl.createUser(param);
        Assertions.assertEquals(1, result);
    }
}
