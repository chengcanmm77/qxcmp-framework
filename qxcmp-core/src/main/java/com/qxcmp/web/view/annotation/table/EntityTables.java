package com.qxcmp.web.view.annotation.table;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityTables {
    EntityTable[] value();
}
