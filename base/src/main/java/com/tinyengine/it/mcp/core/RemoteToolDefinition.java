package com.tinyengine.it.mcp.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class RemoteToolDefinition {
	private String name;
	private String description;
	private JsonNode parameters;   // JSON Schema
	private String serverUrl;
}
