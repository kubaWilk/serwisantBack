package com.jakubwilk.serwisant.api.service;

import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthService {
    String generateToken(Authentication authentication);

    void handleResetPasswordRequest(String email);

    void handlePasswordReset(String token, Map<String, String> newPassword);
}
