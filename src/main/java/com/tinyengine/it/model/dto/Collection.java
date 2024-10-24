package com.tinyengine.it.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Collection.
 */
public class Collection {
    private List<Object> range;
    private List<Object> data;

    /**
     * Instantiates a new Collection.
     */
    public Collection() {
        this.range = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    /**
     * Gets range.
     *
     * @return the range
     */
    public List<Object> getRange() {
        return range;
    }

    /**
     * Sets range.
     *
     * @param range the range
     */
    public void setRange(List<Object> range) {
        this.range = range;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<Object> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<Object> data) {
        this.data = data;
    }
}
