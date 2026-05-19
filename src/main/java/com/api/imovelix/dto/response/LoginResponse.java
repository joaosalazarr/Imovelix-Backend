package com.api.imovelix.dto.response;

public record LoginResponse(
    String accessToken,
    String tokenType,
    Long expiresIn,
    SystemUserResponse user
) {}
