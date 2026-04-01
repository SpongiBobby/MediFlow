package it.mediflow.sportello.validators;

import it.mediflow.sportello.annotations.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final String REGEX_PHONE = "^\\d{10}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return value.trim().matches(REGEX_PHONE);
    }
}
