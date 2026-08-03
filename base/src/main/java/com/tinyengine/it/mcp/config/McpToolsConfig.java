package com.tinyengine.it.mcp.config;


import com.tinyengine.it.mcp.tools.GitFileReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class McpToolsConfig {
	private static final Logger log = LoggerFactory.getLogger(McpToolsConfig.class);


	/**
	 * 方式一：通过 ToolCallbackProvider 批量注册工具
	 */
	@Bean
	@Primary
	public ToolCallbackProvider toolCallbackProvider(GitFileReaderService gitFileReaderService) {


		MethodToolCallbackProvider provider = MethodToolCallbackProvider.builder()
			.toolObjects(gitFileReaderService)
			.build();
		// 立即验证
		log.info("Tool count from provider: " + provider.getToolCallbacks().length);
		return provider;
	}
}
