package com.qxcmp.core.entity;

import com.qxcmp.user.User;

/**
 * 实体删除事件
 *
 * @param <T> 实体类型
 * @author Aaric
 */
public class EntityDeleteEvent<T> extends AbstractEntityEvent<T> {
    public EntityDeleteEvent(User user, T entity) {
        super(user, entity);
    }
}
