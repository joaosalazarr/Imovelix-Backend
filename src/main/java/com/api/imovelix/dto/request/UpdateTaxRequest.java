package com.api.imovelix.dto.request;

import java.math.BigDecimal;

import com.api.imovelix.enums.TaxPeriodicity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record UpdateTaxRequest(
    @Size(max = 100, message = "Name must have at most 100 characters")
    String name,
    String description,
    @DecimalMin(value = "0.00", message = "Base percentage must be positive or zero")
    BigDecimal percentualBase,
    TaxPeriodicity periodicity
) {}
