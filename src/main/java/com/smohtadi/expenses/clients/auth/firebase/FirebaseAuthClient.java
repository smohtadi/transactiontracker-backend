package com.smohtadi.expenses.clients.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.smohtadi.expenses.clients.auth.AuthClient;
import com.smohtadi.expenses.dtos.AuthRequest;
import com.smohtadi.expenses.dtos.AuthResponse;
import com.smohtadi.expenses.dtos.RefreshTokenRequest;
import com.smohtadi.expenses.dtos.RefreshTokenResponse;
import com.smohtadi.expenses.exceptions.InvalidRefreshTokenException;
import com.smohtadi.expenses.exceptions.InvalidSignInCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class FirebaseAuthClient implements AuthClient {
    private static final String API_KEY_PARAM = "key";

    @Value("${firebase.web-api-key}")
    private String webApiKey;
    @Value("${firebase.refresh-token-url}")
    private String REFRESH_TOKEN_BASE_URL;
    @Value("${firebase.sign-in-url}")
    private String SIGN_IN_BASE_URL;
    @Autowired
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthClient(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

//    public void createUser(String email, String password) {
//        FirebaseCreateUserRequest firebaseCreateUserRequest = new FirebaseCreateUserRequest(
//                email, password, true // email verified
//        );
//        try {
//
//        } catch (FirebaseAuthException e) {
//            if (e.getMessage().contains("EMAIL_EXISTS")) {
//                throw new AccountExistsException();
//            }
//            throw e;
//        }
//    }

    public AuthResponse login(AuthRequest authRequest) {
        FirebaseSignInRequest firebaseSignInRequest = new FirebaseSignInRequest(
                authRequest.email(),
                authRequest.password(),
                true);
        try {
            FirebaseSignInResponse firebaseSignInResponse = RestClient.create(SIGN_IN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder.queryParam(API_KEY_PARAM, webApiKey).build())
                    .body(firebaseSignInRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(FirebaseSignInResponse.class);
            assert firebaseSignInResponse != null;
            return new AuthResponse(firebaseSignInResponse.idToken(), firebaseSignInResponse.refreshToken());
        } catch (HttpClientErrorException e) {
            if (e.getResponseBodyAsString().contains("INVALID_LOGIN_CREDENTIALS")) {
                throw new InvalidSignInCredentialsException();
            }
            throw e;
        }
    }

    public RefreshTokenResponse getNewToken(RefreshTokenRequest refreshTokenRequest) {
        FirebaseRefreshTokenRequest firebaseRefreshTokenRequest =
                new FirebaseRefreshTokenRequest(
                        "refresh_token",
                        refreshTokenRequest.refreshToken());
        try {
            FirebaseRefreshTokenResponse firebaseRefreshTokenResponse =
                    RestClient.create(REFRESH_TOKEN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder.queryParam(API_KEY_PARAM, webApiKey).build())
                    .body(firebaseRefreshTokenRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(FirebaseRefreshTokenResponse.class);
            assert firebaseRefreshTokenResponse != null;
            return new RefreshTokenResponse(firebaseRefreshTokenResponse.id_token());
        } catch (HttpClientErrorException e) {
            if (e.getResponseBodyAsString().contains("INVALID_REFRESH_TOKEN")) {
                throw new InvalidRefreshTokenException();
            }
            throw e;
        }
    }

    public void logout(String userId) {
        try {
            firebaseAuth.revokeRefreshTokens(userId);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }
}
