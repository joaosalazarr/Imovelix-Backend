package com.api.imovelix.dto.response;

import com.api.imovelix.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    Long id,
    LocalDateTime dueDate,
    LocalDateTime paymentDate,
    BigDecimal paidAmount,
    PaymentStatus status
) {}
