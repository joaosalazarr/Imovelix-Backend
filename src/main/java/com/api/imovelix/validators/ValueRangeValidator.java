package com.api.imovelix.validators;

import java.math.BigDecimal;

import com.api.imovelix.dto.request.ListPropertiesRequest;
import com.api.imovelix.validators.annotations.ValidValueRange;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueRangeValidator implements ConstraintValidator<ValidValueRange, ListPropertiesRequest> {
    @Override
    public boolean isValid(ListPropertiesRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        boolean saleValueRangeValid = isRangeValid(request.minSaleValue(), request.maxSaleValue());
        boolean totalAreaRangeValid = isRangeValid(request.minTotalArea(), request.maxTotalArea());

        return saleValueRangeValid && totalAreaRangeValid;
    }

    private boolean isRangeValid(BigDecimal minimum, BigDecimal maximum) {
        if (minimum == null || maximum == null) {
            return true;
        }

        return minimum.compareTo(maximum) <= 0;
    }
}
