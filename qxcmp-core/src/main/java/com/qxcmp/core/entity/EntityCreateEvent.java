package com.qxcmp.core.entity;

import com.qxcmp.user.User;

/**
 * 实体新建事件
 *
 * @param <T> 实体类型
 * @author Aaric
 */
public class EntityCreateEvent<T> extends AbstractEntityEvent<T> {
    public EntityCreateEvent(User user, T entity) {
        super(user, entity);
    }
}
