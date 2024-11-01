package com.tinyengine.it.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMe() {
        User mockData = new User();
        when(userService.queryUserById(anyInt())).thenReturn(mockData);

        Result<User> result = userController.me();
        Assertions.assertEquals(mockData, result.getData());
    }
}

