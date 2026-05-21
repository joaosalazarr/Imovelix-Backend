package com.api.imovelix.services.security;

public record AuthenticatedUser(
    Long authenticationId,
    Long userId
) {}
