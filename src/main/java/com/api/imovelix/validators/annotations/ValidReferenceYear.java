package com.api.imovelix.validators.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.api.imovelix.validators.ReferenceYearValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ReferenceYearValidator.class)
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReferenceYear {
    String message() default "Reference year must be valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
