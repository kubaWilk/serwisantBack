package com.jakubwilk.serwisant.api.controller;

import com.jakubwilk.serwisant.api.entity.LoginRequest;
import com.jakubwilk.serwisant.api.service.AuthService;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;


    public AuthController(AuthService authService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> token(@RequestBody LoginRequest loginRequest){
        LOGGER.debug("Auth token request for: " + loginRequest.username());
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return ResponseEntity.ok(authService.generateToken(authentication));
    }

    @PostMapping("/reset/request")
    public ResponseEntity<String> generateResetToken(@RequestBody Map<String,String> email){
        authService.handleResetPasswordRequest(email.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<String> setNewPassword(@PathParam("token") String token, @RequestBody Map<String, String> newPassword){
        authService.handlePasswordReset(token, newPassword);

        return ResponseEntity.ok("Password changed.");
    }

}
