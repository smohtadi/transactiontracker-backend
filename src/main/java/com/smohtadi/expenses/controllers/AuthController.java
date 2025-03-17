package com.smohtadi.expenses.controllers;

import com.smohtadi.expenses.dtos.*;
import com.smohtadi.expenses.services.AuthService;
import com.smohtadi.expenses.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    @Value("${security.domain}")
    private String domain;
    @Value("${security.id-token-key}")
    private String idTokenKey;
    @Value("${security.refresh-token-key}")
    private String refreshTokenKey;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDto me(@NotNull Authentication authentication) {
        return userService.getByUid(authentication.getPrincipal().toString());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(authRequest);
        Cookie tokenIdCookie = new Cookie(this.idTokenKey, authResponse.idToken());
        tokenIdCookie.setDomain(this.domain);
//        tokenIdCookie.setSecure(true);
//        tokenIdCookie.setHttpOnly(true);
        tokenIdCookie.setPath("/");
        response.addCookie(tokenIdCookie);
        Cookie refreshTokenCookie = new Cookie(this.refreshTokenKey, authResponse.refreshToken());
//        refreshTokenCookie.setSecure(true);
//        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setDomain(this.domain);
        refreshTokenCookie.setPath("/auth/refresh-token");
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.getNewToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public void logout(@NotNull Authentication authentication) {
        authService.logout(authentication.getPrincipal().toString());
    }
}
