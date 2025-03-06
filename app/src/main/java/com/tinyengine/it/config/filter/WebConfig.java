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

package com.tinyengine.it.config.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public  class WebConfig implements WebMvcConfigurer {
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter()
    {
        // 跨域配置地址
        List<String> crosDomainList = Arrays.asList(allowedOrigins.split(","));

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1、允许来源
        corsConfiguration.setAllowedOriginPatterns(crosDomainList);
        // 2、允许任何请求头
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        // 3、允许任何方法
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        // 4、允许凭证
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
         return new CorsFilter(source);
         }

}