package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * AiMessages
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class AiMessages {
    private String content;
    private String role;
    private String name;
}
