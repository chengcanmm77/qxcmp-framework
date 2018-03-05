package com.qxcmp.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 用于图片上传验证，确定上传的图片格式是正确的
 *
 * @author aaric
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface Image {

    String message() default "{Image}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
