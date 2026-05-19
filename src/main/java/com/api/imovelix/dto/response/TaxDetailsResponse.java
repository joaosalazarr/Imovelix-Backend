package com.api.imovelix.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.api.imovelix.enums.TaxPeriodicity;

public record TaxDetailsResponse(
    Long id,
    String name,
    String description,
    BigDecimal percentualBase,
    TaxPeriodicity periodicity,
    List<TaxCalculationSummaryResponse> taxCalculations
) {}
