package com.qxcmp.web.view.annotation.form;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NumberField {

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
     * 是否禁用浏览器自动完成
     */
    boolean disableAutoComplete() default false;

    /**
     * 是否在页面加载完成时自动获得焦点
     */
    boolean autoFocus() default false;

    /**
     * 是否为只读
     */
    boolean readOnly() default false;

    /**
     * 提示文本
     */
    String placeholder() default "";

    /**
     * 最小值
     */
    float min() default Float.MIN_VALUE;

    /**
     * 最大值
     */
    float max() default Float.MAX_VALUE;

    /**
     * 步长
     * <p>
     * 每次点击增加或减少按钮的时候改变的值
     */
    float step() default 1F;
}
