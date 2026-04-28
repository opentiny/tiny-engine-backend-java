package com.tinyengine.it.mcp.tools;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class DemoMcpTools {

	@Tool(description = "获取当前服务器时间",name = "current_time")
	public String getCurrentTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	@Tool(description = "计算两个数字的和",name = "calculate_sum")
	public int calculateSum(
		@ToolParam(description = "第一个数字") int a,
		@ToolParam(description = "第二个数字") int b) {
		return a + b;
	}

	@Tool(description = "获取天气信息",name = "get_weather")
	public String getWeather(
		@ToolParam(description = "城市名称") String city) {
		// 模拟天气查询
		Map<String, String> weatherMap = new HashMap<>();
		weatherMap.put("北京", "晴天 25°C");
		weatherMap.put("上海", "多云 22°C");
		weatherMap.put("广州", "阵雨 28°C");
		weatherMap.put("深圳", "晴天 27°C");

		return weatherMap.getOrDefault(city, "未知城市: " + city + "，请尝试北京、上海、广州、深圳");
	}

	@Tool(description = "发送通知消息",name = "send_notification")
	public String sendNotification(
		@ToolParam(description = "接收者") String recipient,
		@ToolParam(description = "消息内容") String message) {
		return String.format("已发送通知给 %s: %s", recipient, message);
	}
}
