package com.smohtadi.expenses.clients.auth.firebase;

public record FirebaseRefreshTokenRequest(String grant_type, String refresh_token) { }
