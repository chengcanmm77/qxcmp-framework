package com.qxcmp.security;

import java.lang.annotation.*;

/**
 * 权限自动加载注解
 * <p>
 * 如果一个Spring Bean使用了该注解，则在平台启动的时候会自动加载以{@link PrivilegeAutowired#prefix} 开头的字段
 * <p>
 * 将该字段自动生成为一项平台权限，并寻找匹配的且以{@link PrivilegeAutowired#suffix}结尾的字段值为权限描述信息
 *
 * @author aaric
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PrivilegeAutowired {

    /**
     * 自动加载字段前缀
     * <p>
     * 若字段名称以此前缀开头，则会被平台自动加载并创建相关权限
     * <p>
     * 权限字段推荐使用修饰符{@code public static final String}
     *
     * @return 自动配置字段前缀
     */
    String prefix() default "PRIVILEGE_";

    /**
     * 权限描述信息后缀
     *
     * @return 权限描述信息后缀
     */
    String suffix() default "_DESCRIPTION";
}
