package com.qxcmp.admin.form;

import java.lang.annotation.*;

/**
 * 表单中的该注解字段会被忽略
 *
 * @author Aaric
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoredSystemConfig {
}
