package com.madi.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madi.backend.utils.security.CookieUtils;
import com.madi.backend.utils.security.JwtTokenUtils;
import com.madi.backend.utils.security.LoginUserRequest;

import jakarta.servlet.http.Cookie;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private CookieUtils cookieUtils;

    @Override
    public Cookie login(LoginUserRequest userRequest) {
        String token = jwtTokenUtils.generateToken(userRequest);
        Cookie cookie = cookieUtils.createCookie(token);
        return cookie;
    }

    @Override
    public Cookie logout() {
        Cookie deleteCookie = cookieUtils.deleteCookie();
        return deleteCookie;
    }
}
