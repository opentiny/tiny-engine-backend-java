package com.tinyengine.it;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * The type Tiny engine application.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.tinyengine.it.mapper")
public class TinyEngineApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TinyEngineApplication.class, args);
    }
}
