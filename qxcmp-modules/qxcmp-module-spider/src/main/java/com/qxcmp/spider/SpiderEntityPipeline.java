package com.qxcmp.spider;

import com.qxcmp.core.entity.AbstractEntityService;
import lombok.AllArgsConstructor;

/**
 * 蜘蛛实体管道
 * <p>
 * 该抽象类用于快速创建于平台实体服务相关的蜘蛛管道
 *
 * @param <T> 领域对象类型
 * @param <R> 领域对象实体服务
 * @author aaric
 * @see SpiderPipeline
 * @see AbstractEntityService
 */
@AllArgsConstructor
public abstract class SpiderEntityPipeline<T, R extends AbstractEntityService> extends SpiderPipeline<T> {

    /**
     * 领域对象实体服务
     */
    protected R entityService;
}
