package com.qxcmp.web.view.annotation.form;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicField {

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
     * 最大个数
     */
    int maxCount() default 4;

    /**
     * 项目表格标题
     * <p>
     * 需要与项目字段对齐
     */
    String[] itemHeaders();

    /**
     * 项目字段
     * <p>
     * 需要编辑的项目字段
     * <p>
     * 如果此值留空则表明为原始类型
     */
    String[] itemFields() default {};
}
