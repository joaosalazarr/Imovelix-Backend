package com.api.imovelix.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.api.imovelix.enums.PropertyPourpose;
import com.api.imovelix.enums.PropertyType;

public record PropertyDetailsResponse(
    Long id,
    PropertyType propertyType,
    PropertyPourpose pourpose,
    String zipCode,
    String street,
    Integer streetNumber,
    String complement,
    String neighborhood,
    String city,
    String state,
    BigDecimal totalArea,
    BigDecimal builtArea,
    BigDecimal saleValue,
    BigDecimal purchaseValue,
    LocalDateTime acquisitionDate,
    Boolean hasFinancing,
    List<TaxCalculationSummaryResponse> taxCalculations
) {}
