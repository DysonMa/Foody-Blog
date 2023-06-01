package com.madi.backend.utils.security;

import lombok.Getter;

@Getter
public class LoginUserRequest {
    String username;
    String password;

    public void setPassword(String password) {
        this.password = PasswordUtils.hashPassword(password);
    }
}
