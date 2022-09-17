package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.validation.ValidEmail;

import javax.validation.constraints.NotBlank;

public class PasswordResetRequest {

    @NotBlank(message = "is required")
    @ValidEmail
    private String email;

    public PasswordResetRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
