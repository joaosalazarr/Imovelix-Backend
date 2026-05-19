package com.api.imovelix.mappers;

import org.springframework.stereotype.Component;

import com.api.imovelix.models.MfaFactor;
import com.api.imovelix.models.MfaRecoveryCode;

@Component
public class MfaRecoveryCodeMapper {
    public MfaRecoveryCode toEntity(String codeHash, MfaFactor mfaFactor) {
        return MfaRecoveryCode.builder()
            .codeHash(codeHash)
            .mfaFactor(mfaFactor)
            .used(false)
            .build();
    }
}
