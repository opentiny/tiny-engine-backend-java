package com.tinyengine.it.mapper;

import com.tinyengine.it.model.entity.App;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Mapper集成测试，使用h2内存数据库
 */
@SpringBootTest
class AppMapperTest {
    @Autowired
    private AppMapper appMapper;

    @Test
    void queryAllApp() {
        List<App> result = appMapper.queryAllApp();
        assertNotNull(result);
        assertEquals(1, result.size());
        App app = result.get(0);
        assertEquals("portal-app", app.getName());
    }
}