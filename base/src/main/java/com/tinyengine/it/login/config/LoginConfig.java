package com.tinyengine.it.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    private final SSOInterceptor ssoInterceptor;

    public LoginConfig(SSOInterceptor ssoInterceptor) {
        this.ssoInterceptor = ssoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ssoInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 注册相关
                        "/register",
                        "/platform-center/api/user/register",
                        "/**/register",

                        // 登录相关
                        "/login",
                        "/**/login"
                );
    }
}


