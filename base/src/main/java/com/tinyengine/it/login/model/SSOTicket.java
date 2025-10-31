package com.tinyengine.it.login.model;

import lombok.Data;

@Data
public class SSOTicket {
    private String token;
    private String username;
    private Long expireTime;
}