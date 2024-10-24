package com.tinyengine.it.config.filter;

import org.slf4j.MDC;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * The type Request id filter.
 */
public class RequestIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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
