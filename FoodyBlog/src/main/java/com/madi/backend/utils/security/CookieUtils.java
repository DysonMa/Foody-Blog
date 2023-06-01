package com.madi.backend.utils.security;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieUtils {
    public Cookie createCookie(String secret) {
        Cookie cookie = new Cookie("AuthToken", secret);

        cookie.setHttpOnly(false);
        cookie.setMaxAge(86400); // seconds
        cookie.setSecure(true);

        return cookie;
    }

    public Cookie deleteCookie() {
        Cookie cookie = new Cookie("AuthToken", null);

        cookie.setHttpOnly(false);
        cookie.setMaxAge(0);
        cookie.setSecure(true);

        return cookie;
    }
}
