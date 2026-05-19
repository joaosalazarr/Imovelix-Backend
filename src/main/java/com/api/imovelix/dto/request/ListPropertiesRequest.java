package com.api.imovelix.dto.request;

import java.math.BigDecimal;

import com.api.imovelix.enums.PropertyPourpose;
import com.api.imovelix.enums.PropertyType;
import com.api.imovelix.validators.annotations.ValidBrazilianState;
import com.api.imovelix.validators.annotations.ValidValueRange;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@ValidValueRange
public record ListPropertiesRequest(
    Long userId,
    PropertyType propertyType,
    PropertyPourpose pourpose,
    @Size(max = 100, message = "City must have at most 100 characters")
    String city,
    @ValidBrazilianState(message = "State must be a valid UF")
    String state,
    Boolean hasFinancing,
    @DecimalMin(value = "0.00", message = "Minimum sale value must be positive")
    BigDecimal minSaleValue,
    @DecimalMin(value = "0.00", message = "Maximum sale value must be positive")
    BigDecimal maxSaleValue,
    @DecimalMin(value = "0.00", message = "Minimum total area must be positive")
    BigDecimal minTotalArea,
    @DecimalMin(value = "0.00", message = "Maximum total area must be positive")
    BigDecimal maxTotalArea,
    @PositiveOrZero(message = "Page must be zero or positive")
    Integer page,
    @Positive(message = "Size must be positive")
    @Max(value = 100, message = "Size must be at most 100")
    Integer size,
    @Size(max = 50, message = "Sort field must have at most 50 characters")
    String sortBy,
    @Pattern(regexp = "^(asc|desc|ASC|DESC)$", message = "Sort direction must be asc or desc")
    String sortDirection
) {
    public Integer pageOrDefault() {
        return page == null ? 0 : page;
    }

    public Integer sizeOrDefault() {
        return size == null ? 20 : size;
    }

    public String sortByOrDefault() {
        return sortBy == null || sortBy.isBlank() ? "id" : sortBy;
    }

    public String sortDirectionOrDefault() {
        return sortDirection == null || sortDirection.isBlank() ? "asc" : sortDirection;
    }
}
