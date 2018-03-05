package com.qxcmp.web.view.annotation.table;

import java.lang.annotation.*;

/**
 * 行操作检查
 * <p>
 * 在实体上标注该注解的方法将会被自动调用
 * <p>
 * 调用的方法必须返回布尔值
 *
 * @author Aaric
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RowActionCheck {

    /**
     * 关联的操作名称
     */
    String value();
}
