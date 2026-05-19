package com.api.imovelix.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.api.imovelix.enums.PaymentStatus;

import jakarta.validation.constraints.DecimalMin;

public record UpdatePaymentRequest(
    LocalDateTime dueDate,
    LocalDateTime paymentDate,
    @DecimalMin(value = "0.00", message = "Paid amount must be positive or zero")
    BigDecimal paidAmount,
    PaymentStatus status
) {}
