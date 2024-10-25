package com.tinyengine.it.model.dto;

import com.tinyengine.it.model.entity.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CanvasDto {
    private String operate;
    private User occupier;
}
