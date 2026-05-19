package com.api.imovelix.validators;

import com.api.imovelix.validators.annotations.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true;
        }

        String cleanedPhoneNumber = phoneNumber.replaceAll("\\D", "");

        if (cleanedPhoneNumber.length() != 10 && cleanedPhoneNumber.length() != 11) {
            return false;
        }

        String areaCode = cleanedPhoneNumber.substring(0, 2);

        if (areaCode.equals("00")) {
            return false;
        }

        if (cleanedPhoneNumber.length() == 11) {
            return cleanedPhoneNumber.charAt(2) == '9';
        }

        return true;
    }
}
