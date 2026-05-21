package com.api.imovelix.dto.response;

public record LoginResponse(
    String accessToken,
    String tokenType,
    Long expiresIn,
    SystemUserResponse user,
    Boolean mfaRequired,
    Long mfaFactorId
) {
    public LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        SystemUserResponse user
    ) {
        this(accessToken, tokenType, expiresIn, user, false, null);
    }
}
