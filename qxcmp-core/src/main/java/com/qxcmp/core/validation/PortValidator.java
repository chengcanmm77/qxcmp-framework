package com.qxcmp.core.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PortValidator implements ConstraintValidator<Port, String> {

    private static final int MIN_PORT_NUMBER = 0;

    private static final int MAX_PORT_NUMBER = 65535;

    @Override
    public void initialize(Port constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value)) {
            return true;
        }

        try {
            Integer port = Integer.parseInt(value);
            return port >= MIN_PORT_NUMBER && port <= MAX_PORT_NUMBER;
        } catch (Exception e) {
            return false;
        }
    }
}
