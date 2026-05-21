package com.api.imovelix.mappers;

import org.springframework.stereotype.Component;

import com.api.imovelix.dto.request.RegisterMfaFactorRequest;
import com.api.imovelix.dto.response.MfaFactorResponse;
import com.api.imovelix.models.MfaFactor;
import com.api.imovelix.models.UserAuthentication;

@Component
public class MfaFactorMapper {
    public MfaFactor toEntity(
        RegisterMfaFactorRequest request,
        String encryptedSecret,
        UserAuthentication userAuthentication
    ) {
        return MfaFactor.builder()
            .type(request.type())
            .encryptedSecret(encryptedSecret)
            .userAuthentication(userAuthentication)
            .active(false)
            .build();
    }

    public MfaFactorResponse toResponse(MfaFactor mfaFactor) {
        return new MfaFactorResponse(
            mfaFactor.getId(),
            mfaFactor.getType(),
            mfaFactor.getActive(),
            mfaFactor.getCreatedAt(),
            mfaFactor.getConfirmedAt(),
            mfaFactor.getLastUsedAt()
        );
    }

    public MfaFactorResponse toSetupResponse(MfaFactor mfaFactor, String setupSecret) {
        return new MfaFactorResponse(
            mfaFactor.getId(),
            mfaFactor.getType(),
            mfaFactor.getActive(),
            mfaFactor.getCreatedAt(),
            mfaFactor.getConfirmedAt(),
            mfaFactor.getLastUsedAt(),
            setupSecret
        );
    }
}
