package com.api.imovelix.dto.response;

import java.time.LocalDateTime;

public record AuthInfoResponse(
    String email,
    Boolean mfaEnabled,
    Boolean emailVerified,
    LocalDateTime lastLogin,
    LocalDateTime passwordUpdatedAt
) {}
