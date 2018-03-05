package com.qxcmp.web.view.annotation.form;

import com.qxcmp.web.view.modules.form.FormEnctype;
import com.qxcmp.web.view.modules.form.FormMethod;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Size;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Form {

    /**
     * 表单标题和表单提交操作名称
     */
    String value() default "";

    /**
     * 表单对象名称
     * <p>
     * 表单对象需要另外手动添加到 ModelAndView 中
     * <p>
     * 表单必须包裹一个表单对象用于存储表单提交数据
     * <p>
     * 默认为表单对象类型小写驼峰规则
     */
    String objectName() default "";

    /**
     * 名称
     */
    String name() default "";

    /**
     * 提交链接
     */
    String action() default "";

    /**
     * 提交方式
     */
    FormMethod method() default FormMethod.POST;

    /**
     * 编码方式
     */
    FormEnctype enctype() default FormEnctype.NONE;

    /**
     * 是否禁用自动完成
     */
    boolean disableAutoComplete() default false;

    /**
     * 提交打开方式
     */
    AnchorTarget target() default AnchorTarget.NONE;

    /**
     * 大小
     */
    Size size() default Size.NONE;

    /**
     * 是否翻转颜色
     */
    boolean inverted() default false;

    /**
     * 提交文本
     */
    String submitText() default "";

    /**
     * 提交图标
     */
    String submitIcon() default "";
}
