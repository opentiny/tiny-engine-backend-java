package com.tinyengine.it.model.dto;

import lombok.Data;
import java.util.List;

/**
 * Child
 *
 * @since 2024-11-13
 */
@Data
public class Child {
    List<Snippet> children;
    String group;
}
