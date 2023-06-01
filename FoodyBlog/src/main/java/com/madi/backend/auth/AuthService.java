package com.madi.backend.auth;

import com.madi.backend.utils.security.LoginUserRequest;

import jakarta.servlet.http.Cookie;

public interface AuthService {
    Cookie login(LoginUserRequest userRequest);

    Cookie logout();
}
