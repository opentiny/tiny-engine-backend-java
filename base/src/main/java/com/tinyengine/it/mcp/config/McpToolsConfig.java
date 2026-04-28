package com.tinyengine.it.mcp.config;

import com.tinyengine.it.mcp.tools.DemoMcpTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class McpToolsConfig {

	/**
	 * 方式一：通过 ToolCallbackProvider 批量注册工具
	 */
	@Bean
	@Primary
	public ToolCallbackProvider toolCallbackProvider(DemoMcpTools demoMcpTools) {


		MethodToolCallbackProvider provider = MethodToolCallbackProvider.builder()
			.toolObjects(demoMcpTools)
			.build();
		// 立即验证
		System.out.println("Tool count from provider: " + provider.getToolCallbacks().length);
		return provider;
	}
}
