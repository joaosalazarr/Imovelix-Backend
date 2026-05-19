package com.api.imovelix.exceptions;

public record FieldErrorResponse(
    String field,
    String message,
    Object rejectedValue
) {}
