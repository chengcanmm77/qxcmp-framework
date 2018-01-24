package com.qxcmp.web.view.annotation.table;

import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.AnchorTarget;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(TableFields.class)
public @interface TableField {

    /**
     * 字段名称
     */
    String value();

    /**
     * 顺序
     */
    int order() default Integer.MAX_VALUE;

    /**
     * 字段属于哪个表格
     */
    String name() default "";

    /**
     * 字段描述
     */
    String description() default "";

    /**
     * 字段后缀
     */
    String fieldSuffix() default "";

    /**
     * 如果字段为集合元素，则最多显示多少个集合类型
     */
    int maxCollectionCount() default 5;

    /**
     * 集合元素的索引
     */
    String collectionEntityIndex() default "";

    /**
     * 是否显示为图片
     */
    boolean image() default false;

    /**
     * 是否渲染为超链接
     */
    boolean enableUrl() default false;

    /**
     * 超链接前缀
     */
    String urlPrefix() default "";

    /**
     * 超链接实体索引
     */
    String urlEntityIndex() default "";

    /**
     * 超链接后缀
     */
    String urlSuffix() default "";

    /**
     * 超链接打开方式
     */
    AnchorTarget urlTarget() default AnchorTarget.SELF;

    /**
     * 单元格对齐
     */
    Alignment alignment() default Alignment.CENTER;

}
