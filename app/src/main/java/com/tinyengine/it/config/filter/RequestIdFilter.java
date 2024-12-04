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

import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The type Request id filter.
 *
 * @since 2024-10-20
 */
public class RequestIdFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        // 初始化方法
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 生成唯一请求 ID
            String requestId = UUID.randomUUID().toString();
            // 将请求 ID 存储到 MDC 中
            MDC.put("requestId", requestId);
            // 继续处理请求
            chain.doFilter(request, response);
        } finally {
            // 清除 MDC 中的请求 ID，避免泄漏
            MDC.remove("requestId");
        }
    }

    @Override
    public void destroy() {
        // 清理资源
    }
}
