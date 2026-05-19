package com.api.imovelix.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TaxCalculationResponse (
    Long id,
    Integer refferenceYear,
    BigDecimal baseValue,
    BigDecimal appliedPercentage,
    BigDecimal taxValue,
    LocalDateTime calculatedAt,
    TaxSummaryResponse taxSummary,
    PropertySummaryResponse propertySummary,
    List<PaymentResponse> payments
) { }
