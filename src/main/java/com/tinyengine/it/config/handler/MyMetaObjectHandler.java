package com.tinyengine.it.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * The type My meta object handler.
 *
 * @since 2024-10-20
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("lastUpdatedTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createdBy", "1", metaObject);
        this.setFieldValByName("lastUpdatedBy", "1", metaObject);
        this.setFieldValByName("tenantId", "1", metaObject);
        this.setFieldValByName("siteId", "1", metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("lastUpdatedTime", LocalDateTime.now(), metaObject);
    }
}

