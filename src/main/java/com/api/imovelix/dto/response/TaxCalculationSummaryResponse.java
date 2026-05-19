package com.api.imovelix.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TaxCalculationSummaryResponse(
    Long id,
    Integer refferenceYear,
    BigDecimal taxValue,
    LocalDateTime calculatedAt
) {}
