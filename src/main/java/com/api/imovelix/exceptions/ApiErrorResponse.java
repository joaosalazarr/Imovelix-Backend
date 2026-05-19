package com.api.imovelix.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String message,
    String path,
    List<FieldErrorResponse> fieldErrors
) {
    public static ApiErrorResponse of(Integer status, String error, String message, String path) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message, path, List.of());
    }

    public static ApiErrorResponse of(
        Integer status,
        String error,
        String message,
        String path,
        List<FieldErrorResponse> fieldErrors
    ) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message, path, fieldErrors);
    }
}
