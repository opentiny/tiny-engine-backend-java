package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The type Ai param.
 * @since 2024-10-20
 *
 */
@Data
public class AiParam {
    private Map<String, String> foundationModel;
    private List<Map<String, String>> messages;
}
