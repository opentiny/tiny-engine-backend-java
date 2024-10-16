package com.tinyengine.it;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.tinyengine.it.mapper")
public class TinyEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(TinyEngineApplication.class, args);
    }
}
