package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * TreeNodeCollection
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class TreeNodeCollection {
    private List<Integer> range;
    private List<NodeData> data;
}
