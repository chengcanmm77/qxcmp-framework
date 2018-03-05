package com.qxcmp.config;

import java.lang.annotation.*;

/**
 * 系统配置自动加载注解
 * <p>
 * 如果一个Spring Bean使用了该注解，则在平台启动的时候会自动加载以{@link SystemConfigAutowired#prefix} 开头的字段
 * <p>
 * 将该字段自动生成为系统配置，并寻找匹配的且以{@link SystemConfigAutowired#suffix}结尾的字段值为默认值
 *
 * @author aaric
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SystemConfigAutowired {

    /**
     * 自动加载字段前缀
     * <p>
     * 若字段名称以此前缀开头，则会被平台自动加载并创建相关系统配置
     * <p>
     * 系统配置字段推荐使用修饰符{@code public static String}
     *
     * @return 自动配置字段前缀
     */
    String prefix() default "SYSTEM_CONFIG_";

    /**
     * 系统配置默认值后缀
     *
     * @return 系统配置默认值后缀
     */
    String suffix() default "_DEFAULT_VALUE";
}
