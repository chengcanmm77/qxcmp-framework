package com.qxcmp.web.view.page;

import java.lang.annotation.*;

/**
 * 标注该注解的页面会在移动端的时候被渲染为移动端页面
 *
 * @author Aaric
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Page
public @interface MobilePage {

}
