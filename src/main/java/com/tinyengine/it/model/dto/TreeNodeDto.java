package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * TreeNodeDto
 *
 * @since 2024-10-20
 */
@Setter
@Getter
public class TreeNodeDto {
    private List<Integer> pids;
    private Integer level;
    private TreeNodeCollection collection;
}
