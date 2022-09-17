package com.datn.coworkingspace.dto;

public class TokenRefreshResponse extends JwtResponse{

    public TokenRefreshResponse(String token, String refreshToken, Long id, String username, String email) {
        super(token, refreshToken, id, username, email);
    }
}
