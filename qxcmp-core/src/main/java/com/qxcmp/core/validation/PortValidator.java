package com.qxcmp.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Aaric
 */
public class PortValidator implements ConstraintValidator<Port, Integer> {

    private static final int MIN_PORT_NUMBER = 0;

    private static final int MAX_PORT_NUMBER = 65535;

    @Override
    public void initialize(Port constraintAnnotation) {

    }

    @Override
    public boolean isValid(Integer port, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(port)) {
            return true;
        }
        return port >= MIN_PORT_NUMBER && port <= MAX_PORT_NUMBER;
    }
}
