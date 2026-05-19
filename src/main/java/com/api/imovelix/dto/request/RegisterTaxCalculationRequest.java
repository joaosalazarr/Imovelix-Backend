package com.api.imovelix.dto.request;

import java.math.BigDecimal;

import com.api.imovelix.validators.annotations.ValidReferenceYear;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterTaxCalculationRequest(
    @NotNull(message = "Property id is required")
    @Positive(message = "Property id must be positive")
    Long propertyId,
    @NotNull(message = "Tax id is required")
    @Positive(message = "Tax id must be positive")
    Long taxId,
    @NotNull(message = "Reference year is required")
    @ValidReferenceYear(message = "Reference year must be valid")
    Integer refferenceYear,
    @NotNull(message = "Base value is required")
    @DecimalMin(value = "0.00", message = "Base value must be positive or zero")
    BigDecimal baseValue,
    @NotNull(message = "Applied percentage is required")
    @DecimalMin(value = "0.00", message = "Applied percentage must be positive or zero")
    BigDecimal appliedPercentage
) {}
