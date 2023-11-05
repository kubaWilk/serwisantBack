package com.jakubwilk.serwisant.api.controller;

import com.jakubwilk.serwisant.api.entity.LoginRequest;
import com.jakubwilk.serwisant.api.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> token(@RequestBody LoginRequest loginRequest){
        LOGGER.debug("Auth token request for: " + loginRequest.username());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        return ResponseEntity.ok(authService.generateToken(authentication));
    }

    @PostMapping("/reset")
    public ResponseEntity<String> generateResetToken(@RequestBody Map<String,String> email){
        authService.resetPassword(email.get("email"));
        return ResponseEntity.ok().build();
    }
}
