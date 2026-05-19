package com.api.imovelix.dto.request;

import java.time.LocalDateTime;

import com.api.imovelix.enums.PaymentStatus;

import jakarta.validation.constraints.NotNull;

public record UpdatePaymentStatusRequest(
    @NotNull(message = "Status is required")
    PaymentStatus status,
    LocalDateTime paymentDate
) {}
