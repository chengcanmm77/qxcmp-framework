package com.qxcmp.core.entity;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * 实体更新事件
 *
 * @param <T> 实体类型
 * @author Aaric
 */
@Getter
public class EntityUpdateEvent<T> extends AbstractEntityEvent<T> {

    /**
     * 更新前的实体
     */
    private final T originEntity;

    public EntityUpdateEvent(User user, T entity, T originEntity) {
        super(user, entity);
        this.originEntity = originEntity;
    }
}
