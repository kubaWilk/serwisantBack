package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.dao.TokenRepository;
import com.jakubwilk.serwisant.api.entity.PasswordResetToken;
import com.jakubwilk.serwisant.api.entity.User;
import com.jakubwilk.serwisant.api.service.event.events.ResetPasswordEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthServiceDefault implements AuthService {
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    private final ApplicationEventPublisher eventPublisher;

    public AuthServiceDefault(JwtEncoder jwtEncoder,
                              UserService userService,
                              TokenRepository tokenRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public void handleResetPasswordRequest(String email) {
        User user = userService.findByEmail(email);
        PasswordResetToken token;

        token = createResetPasswordToken(user);

        ResetPasswordEvent event =
                new ResetPasswordEvent(token, "Generated a resetPasswordToken");
        eventPublisher.publishEvent(event);
    }

    private PasswordResetToken createResetPasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken newToken = new PasswordResetToken(token, user);

        PasswordResetToken existingToken = tokenRepository.findByUserId(user.getId());

        if(existingToken != null){
            tokenRepository.delete(existingToken);
        }

        return tokenRepository.save(newToken);
    }

    @Override
    public void handlePasswordReset(String token, Map<String, String> newPassword) {
        String email = newPassword.get("email");
        String password = newPassword.get("password");

        if(email.isEmpty() || password.isEmpty()){
            throw new IllegalArgumentException("E-Mail or password invalid!");
        }

        PasswordResetToken tokenInDb = tokenRepository.findByToken(token);
        if(!Objects.equals(tokenInDb.getUser().getEmail(), email)){
            throw new RuntimeException("Provided username doesn't match token's owner");
        }

        userService.changePassword(email, password);
    }
}