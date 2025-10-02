package com.fpt_be.fpt_be.Request;

public class ForgotPasswordRequest {
    private String email;

    // Default constructor
    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
