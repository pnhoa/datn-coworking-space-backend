package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.validation.ValidEmail;

import javax.validation.constraints.NotBlank;

public class PasswordResetChangeRequest {

    @NotBlank(message = "is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "is required.")
    private String password;

    public PasswordResetChangeRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
