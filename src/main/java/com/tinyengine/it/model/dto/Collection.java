package com.tinyengine.it.model.dto;

import java.util.ArrayList;
import java.util.List;

public class Collection {
    private List<Object> range;
    private List<Object> data;

    public List<Object> getRange() {
        return range;
    }

    public void setRange(List<Object> range) {
        this.range = range;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Collection() {
        this.range = new ArrayList<>();
        this.data = new ArrayList<>();
    }
}
