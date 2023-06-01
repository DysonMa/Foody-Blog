package com.madi.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madi.backend.utils.security.LoginUserRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginUserRequest userRequest,
            HttpServletResponse response) {
        Cookie cookie = authService.login(userRequest);
        response.addCookie(cookie);
        return ResponseEntity.ok().body("Successfully login");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = "AuthToken", required = false) String AuthToken,
            HttpServletResponse response) {
        Cookie deleteCookie = authService.logout();
        response.addCookie(deleteCookie);
        return ResponseEntity.ok().body("Successfully logout");
    }
}
