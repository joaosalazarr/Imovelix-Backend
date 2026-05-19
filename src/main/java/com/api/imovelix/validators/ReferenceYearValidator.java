package com.api.imovelix.validators;

import java.time.Year;

import com.api.imovelix.validators.annotations.ValidReferenceYear;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReferenceYearValidator implements ConstraintValidator<ValidReferenceYear, Integer> {
    private static final int MIN_YEAR = 1900;
    private static final int FUTURE_YEAR_LIMIT = 1;

    @Override
    public boolean isValid(Integer referenceYear, ConstraintValidatorContext context) {
        if (referenceYear == null) {
            return true;
        }

        int maxYear = Year.now().getValue() + FUTURE_YEAR_LIMIT;

        return referenceYear >= MIN_YEAR && referenceYear <= maxYear;
    }
}
