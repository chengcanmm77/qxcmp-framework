package com.qxcmp.web.view.annotation.form;

import com.qxcmp.web.view.modules.form.support.DateTimeFieldType;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateTimeField {

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
     * 类型
     */
    DateTimeFieldType type() default DateTimeFieldType.DATETIME;

    /**
     * 是否显示今日按钮
     */
    boolean showToday() default false;

    /**
     * 是否显示上午下午
     */
    boolean showAmPm() default false;

    /**
     * 是否关闭年选择
     */
    boolean disableYear() default false;

    /**
     * 是否关闭月选择
     */
    boolean disableMonth() default false;

    /**
     * 是否关闭分钟选择
     */
    boolean disableMinute() default false;

    /**
     * 是否限制日期选择范围
     */
    boolean enableDateRange() default false;

    /**
     * 当开启日期选择范围的时候生效
     * <p>
     * 指定从今日开始的偏移值基本值
     */
    int dateRangeBaseOffset() default 0;

    /**
     * 当开启日期选择范围的时候生效
     * <p>
     * 指定可以选择的返回长度
     * <p>
     * 必须大于0
     */
    int dateRangeLength() default 1;

}
