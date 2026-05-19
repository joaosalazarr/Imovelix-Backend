package com.api.imovelix.validators.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.api.imovelix.validators.BrazilianStateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = BrazilianStateValidator.class)
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBrazilianState {
    String message() default "State must be a valid Brazilian UF";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
