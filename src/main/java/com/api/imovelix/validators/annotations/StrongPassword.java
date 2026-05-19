package com.api.imovelix.validators.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.api.imovelix.validators.StrongPasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Password must have between 8 and 72 characters and include letters and numbers";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
