package com.api.imovelix.dto.request;

import com.api.imovelix.validators.annotations.ValidCpf;
import com.api.imovelix.validators.annotations.ValidPhoneNumber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateSystemUserRequest(
    @Size(max = 100, message = "Name must have at most 100 characters")
    String name,
    @Pattern(regexp = "^\\d{11}$", message = "CPF must be valid")
    @ValidCpf(message = "CPF must be valid")
    String cpf,
    @Email(message = "E-mail must be valid")
    String email,
    @ValidPhoneNumber(message = "Phone number must be valid")
    String phoneNumber,
    Boolean active
) {}
