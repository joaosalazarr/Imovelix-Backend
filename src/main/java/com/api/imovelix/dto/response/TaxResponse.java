package com.api.imovelix.dto.response;

import java.math.BigDecimal;

import com.api.imovelix.enums.TaxPeriodicity;

public record TaxResponse(
    Long id,
    String name,
    String description,
    BigDecimal percentualBase,
    TaxPeriodicity periodicity
) {}
