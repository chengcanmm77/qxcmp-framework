package com.qxcmp.core.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String> {

    @Override
    public void initialize(Username constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        if (!Character.isAlphabetic(value.charAt(0)) && value.charAt(0) != '_') {
            return false;
        }

        for (char c : value.toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_') {
                return false;
            }
        }

        return true;
    }
}
