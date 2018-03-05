package com.qxcmp.web.view.annotation.form;

import com.qxcmp.web.view.modules.form.field.BooleanFieldStyle;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BooleanField {

    /**
     * 标签名称
     */
    String value();

    /**
     * 字段所属组
     */
    String section() default "";

    /**
     * 字段说明文本
     */
    String tooltip() default "";

    /**
     * 是否为必选项
     * <p>
     * 会在标签上加上红星
     */
    boolean required() default false;

    /**
     * 样式
     */
    BooleanFieldStyle style() default BooleanFieldStyle.STANDARD;

}
