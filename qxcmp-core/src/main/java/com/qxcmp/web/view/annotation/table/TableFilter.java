package com.qxcmp.web.view.annotation.table;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(TableFilters.class)
public @interface TableFilter {

    /**
     * 字段名称
     */
    String value();

}
