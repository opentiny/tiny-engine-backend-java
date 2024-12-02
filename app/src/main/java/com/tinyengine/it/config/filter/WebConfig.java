package com.tinyengine.it.config.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public  class WebConfig implements WebMvcConfigurer {
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.exposed-headers}")
    private String exposedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置 CORS
        registry.addMapping("/**")  // 允许所有路径
                .allowedOrigins(allowedOrigins)  // 允许特定来源的前端地址
                .allowedMethods(allowedMethods.split(","))  // 允许的 HTTP 方法
                .allowedHeaders(allowedHeaders.split(","))  // 允许的请求头
                .exposedHeaders(exposedHeaders.split(","))  // 暴露给前端的响应头
                .allowCredentials(allowCredentials);  // 允许携带凭证
    }
}
