package com.api.imovelix.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterPaymentRequest(
    @NotNull(message = "Tax calculation id is required")
    @Positive(message = "Tax calculation id must be positive")
    Long taxCalculationId,
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be in the present or future")
    LocalDateTime dueDate,
    @NotNull(message = "Paid amount is required")
    @DecimalMin(value = "0.00", message = "Paid amount must be positive or zero")
    BigDecimal paidAmount
) {}
