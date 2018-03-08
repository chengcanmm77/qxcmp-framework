package com.qxcmp.core.entity;

import com.qxcmp.core.event.QxcmpEvent;
import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;


/**
 * 实体事件抽象类
 *
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractEntityEvent<T> implements QxcmpEvent, ResolvableTypeProvider {

    /**
     * 实体的所有者
     */
    private final User user;

    /**
     * 触发事件的实体
     */
    private final T entity;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(entity));
    }
}
