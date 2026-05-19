package com.api.imovelix.dto.response;

public record SystemUserResponse(
    Long id,
    String name,
    String cpf,
    String phoneNumber,
    String email,
    Boolean active
) {}
