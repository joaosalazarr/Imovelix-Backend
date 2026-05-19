package com.api.imovelix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UseMfaRecoveryCodeRequest(
    @NotNull(message = "MFA factor id is required")
    @Positive(message = "MFA factor id must be positive")
    Long mfaFactorId,
    @NotBlank(message = "Recovery code is required")
    String recoveryCode
) {}
