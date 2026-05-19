package com.api.imovelix.validators.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.api.imovelix.validators.ValueRangeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValueRangeValidator.class)
@Target({
    ElementType.TYPE,
    ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidValueRange {
    String message() default "Minimum values must be less than or equal to maximum values";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
