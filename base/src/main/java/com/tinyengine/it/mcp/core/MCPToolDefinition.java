package com.tinyengine.it.mcp.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class MCPToolDefinition {
	private String name;
	private String description;
	private Object bean;
	private Method method;
	private JsonNode inputSchema;  // JSON Schema 格式

	/**
	 * 执行工具方法
	 * @param arguments
	 * @return
	 * @throws Exception
	 */
	public Object execute(JsonNode arguments) throws Exception {
		// 参数转换：根据方法参数类型从 JsonNode 中提取值
		// 这里简化：假设方法只有一个参数，且为 JsonNode 或 String
		Class<?>[] paramTypes = method.getParameterTypes();
		Object[] args = new Object[paramTypes.length];
		for (int i = 0; i < paramTypes.length; i++) {
			Class<?> type = paramTypes[i];
			if (type == JsonNode.class) {
				args[i] = arguments;
			} else if (type == String.class) {
				args[i] = arguments.toString();
			} else {
				// 可以扩展更复杂的转换，例如使用 ObjectMapper 转换为指定类型
				args[i] = null;
			}
		}
		return method.invoke(bean, args);
	}
}
