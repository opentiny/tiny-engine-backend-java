package com.tinyengine.it.dynamic.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DynamicQuery {

    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$", message = "模型名称格式不正确")
    private String nameEn;
    private String nameCh;
    private List<
            @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$", message = "字段名格式不正确")
            String> fields;
    private Map<String, Object> params;
    private Integer currentPage = 1;
    private Integer pageSize = 10;

    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$", message = "排序字段格式不正确")
    private String orderBy;

    @Pattern(regexp = "^(?i)(ASC|DESC)$", message = "排序方式只能是 ASC 或 DESC")
    private String orderType = "ASC";
}
