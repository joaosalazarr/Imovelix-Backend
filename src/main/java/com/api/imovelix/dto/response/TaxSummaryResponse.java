package com.api.imovelix.dto.response;

import java.math.BigDecimal;

import com.api.imovelix.enums.TaxPeriodicity;

public record TaxSummaryResponse(
    Long id,
    String name,
    BigDecimal percentualBase,
    TaxPeriodicity periodicity
) {}
