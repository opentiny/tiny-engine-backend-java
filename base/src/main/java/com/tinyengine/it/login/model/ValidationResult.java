package com.tinyengine.it.login.model;

import lombok.Data;

@Data
public class ValidationResult {
    private boolean valid;
    private String username;

    public ValidationResult(boolean valid, String username) {
        this.valid = valid;
        this.username = username;
    }
}
