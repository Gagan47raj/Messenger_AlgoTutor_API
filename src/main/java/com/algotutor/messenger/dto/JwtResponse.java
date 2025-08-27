package com.algotutor.messenger.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UserDTO user;

    // Constructors
    public JwtResponse() {}

    public JwtResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}
