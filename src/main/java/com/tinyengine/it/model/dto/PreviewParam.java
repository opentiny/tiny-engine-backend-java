package com.tinyengine.it.model.dto;

import lombok.Data;

/**
 * The type Preview param.
 * @since 2024-10-20
 */
@Data
public class PreviewParam {
    private Integer platform;
    private Integer app;
    private String type;
    private Integer id;
}
