package com.fpt_be.fpt_be.Security;

public class UserPrincipal {
    private Long userId;

    public UserPrincipal(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
} 