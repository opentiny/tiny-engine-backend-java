package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class TreeNodeDto {
    private List<Integer> pids;
    private Integer level;
    private TreeNodeCollection collection;
}
