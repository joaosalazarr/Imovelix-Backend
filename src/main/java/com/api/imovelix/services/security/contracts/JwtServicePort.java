package com.api.imovelix.services.security.contracts;

import com.api.imovelix.models.UserAuthentication;

public interface JwtServicePort {
    String generateToken(UserAuthentication authentication);

    long getExpirationSeconds();

    boolean isValid(String token);

    Long extractAuthenticationId(String token);

    Long extractUserId(String token);
}
