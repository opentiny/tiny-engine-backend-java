package com.tinyengine.it.mcp.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tinyengine.it.mcp.annotation.MCPTool;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MCPToolRegistry {
	private final Map<String, MCPToolDefinition> tools = new ConcurrentHashMap<>();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ApplicationContext applicationContext;
	private volatile boolean initialized = false;

	public MCPToolRegistry(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private void initialize() {
		if (initialized) return;
		synchronized (this) {
			if (initialized) return;
			// 扫描所有 bean
			Map<String, Object> beans = applicationContext.getBeansWithAnnotation(org.springframework.stereotype.Component.class);
			for (Object bean : beans.values()) {
				for (Method method : bean.getClass().getDeclaredMethods()) {
					if (method.isAnnotationPresent(MCPTool.class)) {
						registerTool(bean, method);
					}

				}
			}
			initialized = true;
		}
	}

	public MCPToolDefinition getTool(String name) {
		initialize();
		return tools.get(name);
	}

	public Collection<MCPToolDefinition> getAllTools() {
		initialize();
		return tools.values();
	}

	private void registerTool(Object bean, Method method) {
		MCPTool annotation = method.getAnnotation(MCPTool.class);
		MCPToolDefinition def = new MCPToolDefinition();
		def.setName(annotation.name().isEmpty() ? method.getName() : annotation.name());
		def.setDescription(annotation.description());
		def.setBean(bean);
		def.setMethod(method);
		def.setInputSchema(generateSchema(method));
		tools.put(def.getName(), def);
	}

	private JsonNode generateSchema(Method method) {
		ObjectNode schema = objectMapper.createObjectNode();
		schema.put("type", "object");
		ObjectNode properties = objectMapper.createObjectNode();
		for (Parameter param : method.getParameters()) {
			String paramName = param.getName();
			ObjectNode prop = objectMapper.createObjectNode();
			// 简化：假设参数都是 String
			prop.put("type", "string");
			properties.set(paramName, prop);
		}
		schema.set("properties", properties);
		return schema;
	}

}
