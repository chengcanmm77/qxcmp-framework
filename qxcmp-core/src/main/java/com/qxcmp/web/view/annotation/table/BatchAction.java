package com.qxcmp.web.view.annotation.table;


import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Color;

import java.lang.annotation.*;

/**
 * 批处理操作
 * <p>
 * 批处理操作将搭配多选行使用
 * <p>
 * 触发批处理操作以后，会将所有选中的行使用POST请求发送到批处理操作指定的action中
 * <p>
 * 处理完成后自动刷新页面
 *
 * @author Aaric
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BatchAction {

    /**
     * 操作名称
     */
    String value();

    /**
     * 操作Url
     * <p>
     * 实际值为 {@link EntityTable#action()} + 该值 + 所选行的主键
     */
    String action();

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
