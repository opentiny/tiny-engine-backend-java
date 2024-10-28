package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * NodeData
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class NodeData {
    private Integer id;
    private int level;

    public NodeData(Integer id, int level) {
        this.id = id;       // 初始化 id
        this.level = level; // 初始化 level
    }
}
