package com.qxcmp.web.view.annotation.form;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TextSelectionField {

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
     * 选项在 ModelAndView 中的索引
     * <p>
     * 默认为 "selection-items-" 加上字段名称
     */
    String itemIndex() default "";

    /**
     * 选项值索引
     * <p>
     * 用于生成选项值
     */
    String itemValueIndex() default "toString()";

    /**
     * 选项文本索引
     * <p>
     * 用于生成选项文本
     */
    String itemTextIndex() default "toString()";

    /**
     * 是否开启选项搜索
     */
    boolean search() default true;

    /**
     * 多选项最大选项数
     * <p>
     * 当字段为集合类型的时候生效
     */
    int maxSelection() default 0;
}
