package com.jinish.ncsu.sad.userservice.dto;

import lombok.Data;

@Data // Lombok: Creates getters and setters
public class LoginRequest {
    private String email;
    private String password;
}