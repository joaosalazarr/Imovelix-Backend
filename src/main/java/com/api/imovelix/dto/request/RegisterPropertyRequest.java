package com.api.imovelix.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.api.imovelix.enums.PropertyPourpose;
import com.api.imovelix.enums.PropertyType;
import com.api.imovelix.validators.annotations.ValidBrazilianState;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record RegisterPropertyRequest(
    @NotNull(message = "User id is required")
    @Positive(message = "User id must be positive")
    Long userId,
    @NotNull(message = "Property type is required")
    PropertyType propertyType,
    PropertyPourpose pourpose,
    @Size(max = 20, message = "Zip code must have at most 20 characters")
    String zipCode,
    @NotBlank(message = "Street is required")
    @Size(max = 150, message = "Street must have at most 150 characters")
    String street,
    @NotNull(message = "Street number is required")
    @Positive(message = "Street number must be positive")
    Integer streetNumber,
    @Size(max = 100, message = "Complement must have at most 100 characters")
    String complement,
    @NotBlank(message = "Neighborhood is required")
    @Size(max = 100, message = "Neighborhood must have at most 100 characters")
    String neighborhood,
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must have at most 100 characters")
    String city,
    @NotBlank(message = "State is required")
    @ValidBrazilianState(message = "State must be a valid UF")
    String state,
    @NotNull(message = "Total area is required")
    @DecimalMin(value = "0.01", message = "Total area must be greater than zero")
    BigDecimal totalArea,
    @NotNull(message = "Built area is required")
    @DecimalMin(value = "0.00", message = "Built area must be positive or zero")
    BigDecimal builtArea,
    @NotNull(message = "Sale value is required")
    @PositiveOrZero(message = "Sale value must be positive or zero")
    BigDecimal saleValue,
    @NotNull(message = "Purchase value is required")
    @PositiveOrZero(message = "Purchase value must be positive or zero")
    BigDecimal purchaseValue,
    LocalDateTime acquisitionDate,
    Boolean hasFinancing
) {}
