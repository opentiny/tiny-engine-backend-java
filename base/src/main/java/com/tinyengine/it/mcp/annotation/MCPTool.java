package com.tinyengine.it.mcp.annotation;

import java.lang.annotation.*;

/**
 * The interface MCPTool. MCP工具注解，用于标识MCP工具方法，提供工具名称和描述信息。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  MCPTool {
	String name() default "";
	String description() default "";
}
