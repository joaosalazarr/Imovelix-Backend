package com.api.imovelix.dto.response;

public record PropertySummaryResponse(
    Long id,
    String street,
    Integer streetNumber,
    String neighborhood,
    String city,
    String state
) {}
