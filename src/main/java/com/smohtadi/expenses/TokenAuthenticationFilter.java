package com.smohtadi.expenses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseAuth firebaseAuth;
    private final ObjectMapper objectMapper;
    @Value("${security.id-token-key}")
    private String idTokenKey;

    public TokenAuthenticationFilter(FirebaseAuth firebaseAuth, ObjectMapper objectMapper) {
        this.firebaseAuth = firebaseAuth;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String AUTHORIZATION_HEADER = "Authorization";
//        final String BEARER_PREFIX = "Bearer ";
//        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
//        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
//            String token = authorizationHeader.replace(BEARER_PREFIX, "");
//            Optional<String> userId = extractUserIdFromToken(token);
//            if (userId.isPresent()) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                        new UsernamePasswordAuthenticationToken(userId.get(), null, null);
//                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
//                        .buildDetails(request));
//                SecurityContextHolder
//                        .getContext()
//                        .setAuthentication(usernamePasswordAuthenticationToken);
//            } else {
//                setAuthErrorDetails(response);
//                return;
//            }
//        }
        final Cookie[] cookies = request.getCookies() != null ? request.getCookies() : new Cookie[0];
        final String COOKIE_TOKEN_KEY = this.idTokenKey;
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_TOKEN_KEY))
                token = cookie.getValue();
        }
        if (token != null) {
            Optional<String> userId = extractUserIdFromToken(token);
            if (userId.isEmpty()) {
                setAuthErrorDetails(response);
                return;
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userId.get(), null, null);
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractUserIdFromToken(String token) {
        try {
            FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token, true);
            String userId = String.valueOf(firebaseToken.getClaims().get("user_id"));
            return Optional.of(userId);
        } catch (FirebaseAuthException e) {
            return Optional.empty();
        }
    }
    private void setAuthErrorDetails(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication failure: Token missing, invalid, or expired");
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}
