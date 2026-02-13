/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sso配置
 */
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
                "/platform-center/api/user/register",
                // 登录相关
                "/platform-center/api/user/login",
                // 忘记密码
                "/platform-center/api/user/forgot-password",
                // AI
                "/app-center/api/ai/chat",
                "/app-center/api/chat/completions",
                // 图片文件资源下载
                "/material-center/api/resource/download/*",
                //模型驱动
                "/platform-center/api/model-data/**"
            );
    }
}


