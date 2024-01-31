package com.jakubwilk.serwisant.api.controller.auth;

import com.jakubwilk.serwisant.api.entity.LoginRequest;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import com.jakubwilk.serwisant.api.service.AuthService;
import com.jakubwilk.serwisant.api.service.UserService;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@ControllerAdvice
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    public AuthController(AuthService authService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserService userService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> token(@RequestBody LoginRequest loginRequest){
        LOGGER.debug("Auth token request for: " + loginRequest.username());
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        Map<String, Object> response = new HashMap<>();
        User user = userService.findByUsername(username);

        response.put("token", authService.generateToken(authentication));
        response.put("user", user);

        return ResponseEntity.ok(response);
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

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String,String> password, Principal principal){
        User user = userService.findByUsername(principal.getName());
        userService.changePassword(user.getEmail(), password.get("oldPassword"), password.get("newPassword"));

        return ResponseEntity.ok().build();
    }
}
