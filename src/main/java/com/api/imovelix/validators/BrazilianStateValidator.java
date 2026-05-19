package com.api.imovelix.validators;

import java.util.Set;

import com.api.imovelix.validators.annotations.ValidBrazilianState;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BrazilianStateValidator implements ConstraintValidator<ValidBrazilianState, String> {
    private static final Set<String> VALID_STATES = Set.of(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
        "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
        "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    @Override
    public boolean isValid(String state, ConstraintValidatorContext context) {
        if (state == null || state.isBlank()) {
            return true;
        }

        return VALID_STATES.contains(state.trim().toUpperCase());
    }
}
