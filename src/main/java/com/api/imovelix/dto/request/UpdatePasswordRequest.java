package com.api.imovelix.dto.request;

import com.api.imovelix.validators.annotations.StrongPassword;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
    @NotBlank(message = "Current password is required")
    String currentPassword,
    @NotBlank(message = "New password is required")
    @StrongPassword(message = "New password must have between 8 and 72 characters and include letters and numbers")
    String newPassword
) {}
