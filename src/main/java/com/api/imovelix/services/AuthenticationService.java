package com.api.imovelix.services;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.LoginRequest;
import com.api.imovelix.dto.request.UpdatePasswordRequest;
import com.api.imovelix.dto.response.AuthInfoResponse;
import com.api.imovelix.dto.response.LoginResponse;
import com.api.imovelix.mappers.SystemUserMapper;
import com.api.imovelix.mappers.UserAuthenticationMapper;
import com.api.imovelix.models.UserAuthentication;
import com.api.imovelix.repositories.UserAuthenticationRepository;
import com.api.imovelix.services.security.JwtService;
import com.api.imovelix.services.security.PasswordHasher;

@Service
public class AuthenticationService {
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserAuthenticationMapper userAuthenticationMapper;
    private final SystemUserMapper systemUserMapper;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public AuthenticationService(
        UserAuthenticationRepository userAuthenticationRepository,
        UserAuthenticationMapper userAuthenticationMapper,
        SystemUserMapper systemUserMapper,
        PasswordHasher passwordHasher,
        JwtService jwtService
    ) {
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userAuthenticationMapper = userAuthenticationMapper;
        this.systemUserMapper = systemUserMapper;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        UserAuthentication authentication = userAuthenticationRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!Boolean.TRUE.equals(authentication.getUser().getActive())
            || !passwordHasher.matches(request.password(), authentication.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        authentication.setLastLogin(LocalDateTime.now());
        userAuthenticationRepository.save(authentication);

        return new LoginResponse(
            jwtService.generateToken(authentication),
            "Bearer",
            jwtService.getExpirationSeconds(),
            systemUserMapper.toResponse(authentication.getUser())
        );
    }

    @Transactional(readOnly = true)
    public AuthInfoResponse getAuthInfo(Long authenticationId) {
        return userAuthenticationMapper.toResponse(getEntity(authenticationId));
    }

    @Transactional
    public void updatePassword(Long authenticationId, UpdatePasswordRequest request) {
        UserAuthentication authentication = getEntity(authenticationId);
        if (!passwordHasher.matches(request.currentPassword(), authentication.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid current password");
        }

        authentication.setPasswordHash(passwordHasher.hash(request.newPassword()));
        authentication.setPasswordUpdatedAt(LocalDateTime.now());
        userAuthenticationRepository.save(authentication);
    }

    public UserAuthentication getEntity(Long authenticationId) {
        return userAuthenticationRepository.findById(authenticationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Authentication not found"));
    }
}
