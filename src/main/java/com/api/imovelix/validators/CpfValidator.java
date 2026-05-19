package com.api.imovelix.validators;

import com.api.imovelix.validators.annotations.ValidCpf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {
    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        String cleanedCpf = cpf.replaceAll("\\D", "");

        if (cleanedCpf.length() != 11) {
            return false;
        }

        if (hasAllSameDigits(cleanedCpf)) {
            return false;
        }

        int firstDigit = calculateDigit(cleanedCpf.substring(0, 9), 10);
        int secondDigit = calculateDigit(cleanedCpf.substring(0, 9) + firstDigit, 11);

        return cleanedCpf.equals(cleanedCpf.substring(0, 9) + firstDigit + secondDigit);
    }

    private boolean hasAllSameDigits(String cpf) {
        return cpf.chars().allMatch(c -> c == cpf.charAt(0));
    }

    private int calculateDigit(String cpfPart, int weight) {
        int sum = 0;
        for (int i = 0; i < cpfPart.length(); i++) {
            int digit = Character.getNumericValue(cpfPart.charAt(i));
            sum += digit * (weight - i);
        }

        int result = 11 - (sum % 11);

        return result >= 10 ? 0 : result;
    }
}
