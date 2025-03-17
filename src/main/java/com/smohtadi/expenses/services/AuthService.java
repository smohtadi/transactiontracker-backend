package com.smohtadi.expenses.services;

import com.smohtadi.expenses.clients.auth.AuthClient;
import com.smohtadi.expenses.dtos.AuthRequest;
import com.smohtadi.expenses.dtos.AuthResponse;
import com.smohtadi.expenses.dtos.RefreshTokenRequest;
import com.smohtadi.expenses.dtos.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private final AuthClient authClient;

    public AuthService(AuthClient authClient) {
        this.authClient = authClient;
    }

    public RefreshTokenResponse getNewToken(RefreshTokenRequest refreshTokenRequest) {
        return authClient.getNewToken(refreshTokenRequest);
    }

    public AuthResponse login(AuthRequest authRequest) {
        return authClient.login(authRequest);
    }

    public void logout(String userId) {
        authClient.logout(userId);
    }
}
