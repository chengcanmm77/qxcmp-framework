package com.qxcmp.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 网络端口验证 Created by y27chen on 3/22/2017.
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PortValidator.class)
public @interface Port {

    String message() default "{Port}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
