package com.qxcmp.web.view.annotation.table;


import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Color;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableAction {

    /**
     * 操作名称
     */
    String value();

    /**
     * 操作Url
     * <p>
     * 实际值为 {@link EntityTable#action()} + 该值
     */
    String action();

    /**
     * 默认为超链接
     * <p>
     * 操作如果为表单提交
     * <p>
     * 指定表单的提交方式
     */
    FormMethod method() default FormMethod.NONE;

    /**
     * 操作的打开方式
     */
    AnchorTarget target() default AnchorTarget.NONE;

    /**
     * 是否开启确认对话框
     * <p>
     * 当操作为POST请求时生效
     */
    boolean showConfirmDialog() default false;

    /**
     * 当开启确认对话框时，对话框的标题
     */
    String confirmDialogTitle() default "";

    /**
     * 当开启确认对话框时，对话框的文本
     */
    String confirmDialogDescription() default "";

    /**
     * 按钮颜色
     */
    Color color() default Color.NONE;

    /**
     * 是否为主要按钮
     */
    boolean primary() default false;

    /**
     * 是否为次要按钮
     */
    boolean secondary() default false;

    /**
     * 是否翻转颜色
     */
    boolean inverted() default false;

    /**
     * 是否为基本按钮
     */
    boolean basic() default false;
}
