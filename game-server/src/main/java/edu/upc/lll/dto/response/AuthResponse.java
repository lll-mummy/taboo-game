package edu.upc.lll.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private String message;
}