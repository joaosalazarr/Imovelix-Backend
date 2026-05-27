package com.api.imovelix.services;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.imovelix.dto.request.LoginRequest;
import com.api.imovelix.dto.request.UpdatePasswordRequest;
import com.api.imovelix.dto.request.VerifyMfaRequest;
import com.api.imovelix.dto.response.AuthInfoResponse;
import com.api.imovelix.dto.response.LoginResponse;
import com.api.imovelix.mappers.SystemUserMapper;
import com.api.imovelix.mappers.UserAuthenticationMapper;
import com.api.imovelix.models.MfaFactor;
import com.api.imovelix.models.UserAuthentication;
import com.api.imovelix.repositories.MfaFactorRepository;
import com.api.imovelix.repositories.UserAuthenticationRepository;
import com.api.imovelix.services.contracts.AuthenticationServicePort;
import com.api.imovelix.services.security.contracts.CurrentUserPort;
import com.api.imovelix.services.security.contracts.JwtServicePort;
import com.api.imovelix.services.security.contracts.LoginAttemptPort;
import com.api.imovelix.services.security.contracts.PasswordHasherPort;
import com.api.imovelix.services.security.contracts.SecretEncryptionPort;
import com.api.imovelix.services.security.contracts.TotpServicePort;

@Service
public class AuthenticationService implements AuthenticationServicePort {
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserAuthenticationMapper userAuthenticationMapper;
    private final MfaFactorRepository mfaFactorRepository;
    private final SystemUserMapper systemUserMapper;
    private final PasswordHasherPort passwordHasher;
    private final JwtServicePort jwtService;
    private final CurrentUserPort currentUserService;
    private final LoginAttemptPort loginAttemptService;
    private final TotpServicePort totpService;
    private final SecretEncryptionPort secretEncryptionService;

    public AuthenticationService(
        UserAuthenticationRepository userAuthenticationRepository,
        UserAuthenticationMapper userAuthenticationMapper,
        MfaFactorRepository mfaFactorRepository,
        SystemUserMapper systemUserMapper,
        PasswordHasherPort passwordHasher,
        JwtServicePort jwtService,
        CurrentUserPort currentUserService,
        LoginAttemptPort loginAttemptService,
        TotpServicePort totpService,
        SecretEncryptionPort secretEncryptionService
    ) {
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userAuthenticationMapper = userAuthenticationMapper;
        this.mfaFactorRepository = mfaFactorRepository;
        this.systemUserMapper = systemUserMapper;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
        this.currentUserService = currentUserService;
        this.loginAttemptService = loginAttemptService;
        this.totpService = totpService;
        this.secretEncryptionService = secretEncryptionService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request, String clientIp) {
        String rateLimitKey = loginKey(request.email(), clientIp);
        loginAttemptService.assertAllowed(rateLimitKey);

        UserAuthentication authentication = userAuthenticationRepository.findByEmail(request.email())
            .orElseThrow(() -> {
                loginAttemptService.recordFailure(rateLimitKey);
                return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            });

        if (!Boolean.TRUE.equals(authentication.getUser().getActive())
            || !passwordHasher.matches(request.password(), authentication.getPasswordHash())) {
            loginAttemptService.recordFailure(rateLimitKey);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        authentication.setLastLogin(LocalDateTime.now());
        userAuthenticationRepository.save(authentication);
        loginAttemptService.recordSuccess(rateLimitKey);

        if (Boolean.TRUE.equals(authentication.getMfaEnabled())) {
            MfaFactor factor = firstActiveMfaFactor(authentication.getId());
            return new LoginResponse(
                null,
                "MFA_REQUIRED",
                0L,
                systemUserMapper.toResponse(authentication.getUser()),
                true,
                factor.getId()
            );
        }

        return new LoginResponse(
            jwtService.generateToken(authentication),
            "Bearer",
            jwtService.getExpirationSeconds(),
            systemUserMapper.toResponse(authentication.getUser())
        );
    }

    @Transactional
    public LoginResponse verifyMfa(VerifyMfaRequest request) {
        MfaFactor factor = mfaFactorRepository.findById(request.mfaFactorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MFA factor not found"));

        if (!Boolean.TRUE.equals(factor.getActive())
            || !totpService.verify(secretEncryptionService.decrypt(factor.getEncryptedSecret()), request.code())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid MFA code");
        }

        factor.setLastUsedAt(LocalDateTime.now());
        mfaFactorRepository.save(factor);

        UserAuthentication authentication = factor.getUserAuthentication();
        if (!Boolean.TRUE.equals(authentication.getUser().getActive())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return new LoginResponse(
            jwtService.generateToken(authentication),
            "Bearer",
            jwtService.getExpirationSeconds(),
            systemUserMapper.toResponse(authentication.getUser())
        );
    }

    @Transactional(readOnly = true)
    public AuthInfoResponse getAuthInfo(Long authenticationId) {
        currentUserService.requireCurrentAuthentication(authenticationId);
        return userAuthenticationMapper.toResponse(getEntity(authenticationId));
    }

    @Transactional
    public void updatePassword(Long authenticationId, UpdatePasswordRequest request) {
        currentUserService.requireCurrentAuthentication(authenticationId);
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

    private MfaFactor firstActiveMfaFactor(Long authenticationId) {
        return mfaFactorRepository.findByUserAuthenticationIdAndActiveTrue(authenticationId)
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "MFA is enabled without an active factor"));
    }

    private String loginKey(String email, String clientIp) {
        return (email == null ? "" : email.trim().toLowerCase()) + "|" + (clientIp == null ? "" : clientIp);
    }
}
