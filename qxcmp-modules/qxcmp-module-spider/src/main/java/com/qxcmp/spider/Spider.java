package com.qxcmp.spider;

import java.lang.annotation.*;

/**
 * 蜘蛛元数据定义注解
 * <p>
 * 该注解定义了创建一个蜘蛛需要的所有信息
 * <p>
 * 该注解需要定义在{@link SpiderPageProcessor}中
 *
 * @author aaric
 * @see SpiderPageProcessor
 * @see SpiderEntityPipeline
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Spider {

    String group() default "";

    String name();

    String[] startUrls();

    int thread() default 10;

    int order() default Integer.MAX_VALUE;

    boolean disabled() default false;

    Class<? extends SpiderPipeline>[] pipelines();
}
