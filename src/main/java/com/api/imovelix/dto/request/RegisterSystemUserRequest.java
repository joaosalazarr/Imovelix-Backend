package com.api.imovelix.dto.request;

import com.api.imovelix.validators.annotations.ValidCpf;
import com.api.imovelix.validators.annotations.ValidPhoneNumber;
import com.api.imovelix.validators.annotations.StrongPassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record RegisterSystemUserRequest(
    @NotBlank(message = "Name is required")
    String name,
    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "^\\d{11}$", message = "CPF must be valid")
    @ValidCpf(message = "CPF must be valid")
    String cpf,
    @NotBlank(message = "E-mail is required")
    @Email(message = "E-mail must be valid")
    String email,
    @NotBlank(message = "Password is required")
    @StrongPassword(message = "Password must have between 8 and 72 characters and include letters and numbers")
    String password,
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber(message = "Phone number must be valid")
    String phoneNumber
) {}
