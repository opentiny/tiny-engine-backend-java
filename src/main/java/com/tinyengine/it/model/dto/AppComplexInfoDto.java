package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.Apps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class AppComplexInfoDto {
    private String hash;
    private Map<String,Object> meta;
    private Map<String,Object> schema;
    private Apps appInfo;
    private Boolean isChanged;

}
