package com.api.imovelix.dto.response;

import java.time.LocalDateTime;

import com.api.imovelix.enums.MfaType;

public record MfaFactorResponse(
    Long id,
    MfaType type,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime confirmedAt,
    LocalDateTime lastUsedAt,
    String setupSecret
) {
    public MfaFactorResponse(
        Long id,
        MfaType type,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime confirmedAt,
        LocalDateTime lastUsedAt
    ) {
        this(id, type, active, createdAt, confirmedAt, lastUsedAt, null);
    }
}
