package com.BMS.backend.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }
}
