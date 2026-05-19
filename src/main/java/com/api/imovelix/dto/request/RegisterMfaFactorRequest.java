package com.api.imovelix.dto.request;

import com.api.imovelix.enums.MfaType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterMfaFactorRequest(
    @NotNull(message = "User authentication id is required")
    @Positive(message = "User authentication id must be positive")
    Long userAuthenticationId,
    @NotNull(message = "MFA type is required")
    MfaType type
) {}
