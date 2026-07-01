package com.tinyengine.it.modeldata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tinyengine.it.model.dto.ParametersDto;

import java.util.Collections;
import java.util.List;

public class ModelParser {
	public static List<ParametersDto> parseParameters(String parametersJson) {
		if (parametersJson == null || parametersJson.trim().isEmpty()) {
			return Collections.emptyList();
		}
		return JSON.parseObject(parametersJson, new TypeReference<List<ParametersDto>>() {});
	}
}
