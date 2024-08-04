package com.example.dizserver.dto;

import lombok.Data;

@Data
public class AuthenticateResponse {

    private String token;

    private String id;
}
