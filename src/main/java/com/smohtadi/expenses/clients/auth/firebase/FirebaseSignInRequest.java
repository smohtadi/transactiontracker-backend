package com.smohtadi.expenses.clients.auth.firebase;

public record FirebaseSignInRequest(String email, String password, boolean returnSecureToken) {
}
