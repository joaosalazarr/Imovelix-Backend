package com.api.imovelix.mappers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.api.imovelix.dto.request.RegisterSystemUserRequest;
import com.api.imovelix.dto.response.AuthInfoResponse;
import com.api.imovelix.models.SystemUser;
import com.api.imovelix.models.UserAuthentication;

@Component
public class UserAuthenticationMapper {
    public UserAuthentication toEntity(
        RegisterSystemUserRequest request,
        String passwordHash,
        SystemUser systemUser
    ) {
        return UserAuthentication.builder()
            .email(request.email())
            .passwordHash(passwordHash)
            .user(systemUser)
            .mfaEnabled(false)
            .emailVerified(false)
            .passwordUpdatedAt(LocalDateTime.now())
            .build();
    }

    public AuthInfoResponse toResponse(UserAuthentication userAuthentication) {
        return new AuthInfoResponse(
            userAuthentication.getEmail(),
            userAuthentication.getMfaEnabled(),
            userAuthentication.getEmailVerified(),
            userAuthentication.getLastLogin(),
            userAuthentication.getPasswordUpdatedAt()
        );
    }
}
