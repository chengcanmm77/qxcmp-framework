package com.qxcmp.core.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value)) {
            return true;
        }

        if (value.matches("\\d{11}")) {
            return true;
        }

        if (value.matches("\\d{3}-\\d{4}-\\d{4}")) {
            return true;
        }

        if (value.matches("\\d{3} \\d{4} \\d{4}")) {
            return true;
        }

        return false;
    }
}
