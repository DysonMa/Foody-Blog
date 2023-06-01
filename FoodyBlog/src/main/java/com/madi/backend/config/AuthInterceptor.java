package com.madi.backend.config;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.madi.backend.utils.security.JwtTokenUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_TYPE = "Authorization";
    private static final String COOKIE_NAME = "AuthToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse respsone, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            respsone.setStatus(401);
            return false;
        }

        Optional<Cookie> cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(COOKIE_NAME))
                .findFirst();
        String authHeader = request.getHeader(HEADER_TYPE);

        String token = "";
        if (cookie.isPresent()) {
            token = cookie.get().getValue();
        } else if (authHeader != null) {
            token = authHeader.replace(TOKEN_PREFIX, "").trim();
        }

        boolean isValidated = jwtTokenUtils.validateToken(token);
        if (isValidated) {
            return true;
        } else {
            respsone.setStatus(401);
            return false;
        }
    }
}
