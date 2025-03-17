package com.smohtadi.expenses.clients.auth;

import com.smohtadi.expenses.dtos.AuthRequest;
import com.smohtadi.expenses.dtos.AuthResponse;
import com.smohtadi.expenses.dtos.RefreshTokenRequest;
import com.smohtadi.expenses.dtos.RefreshTokenResponse;

public interface AuthClient {
    public RefreshTokenResponse getNewToken(RefreshTokenRequest refreshTokenRequest);
    public AuthResponse login(AuthRequest authRequest);
    public void logout(String userId);
}
