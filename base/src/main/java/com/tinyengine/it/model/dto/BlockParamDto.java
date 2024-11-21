package com.tinyengine.it.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <p>
 * 区块传参dto
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Data
public class BlockParamDto {
    private String appId;
    private String sort;

    @JsonProperty("name_cn")
    private String name;

    private String description;
    private String label;
    private Integer limit;
    private Integer start;
}
