package com.qxcmp.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 用于字符串验证，标有该注解的字符串为常规字符串 不能包含空格 必须以字母或者下划线开头 仅能为字母、数字、下划线的组合 Created by y27chen on 3/22/2017.
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface Username {

    String message() default "{Username}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
