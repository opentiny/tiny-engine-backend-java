
package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Collection.
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class RangeData {
    private List<Object> range;
    private List<Object> data;

    /**
     * Instantiates a new Collection.
     */
    public RangeData() {
        this.range = new ArrayList<>();
        this.data = new ArrayList<>();
    }
}
