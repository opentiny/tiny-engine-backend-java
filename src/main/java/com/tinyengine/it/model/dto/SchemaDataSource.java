package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.Datasource;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * SchemaDataSource
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class SchemaDataSource {
    private Map<String, Object> dataHandler;
    private List<Datasource> list;
}
