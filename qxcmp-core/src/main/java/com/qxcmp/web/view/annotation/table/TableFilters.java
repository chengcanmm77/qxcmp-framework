package com.qxcmp.web.view.annotation.table;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableFilters {
    TableFilter[] value();
}