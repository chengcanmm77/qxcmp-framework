package com.qxcmp.web.view.annotation.table;

import com.qxcmp.web.view.modules.table.AbstractTableCell;

import java.lang.annotation.*;

/**
 * 字段渲染标注
 * <p>
 * 该字段应该如何渲染
 * <p>
 * 调用方法必须返回 {@link AbstractTableCell}
 *
 * @author Aaric
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableFieldRender {

    /**
     * 关联的字段名称
     */
    String value();
}
