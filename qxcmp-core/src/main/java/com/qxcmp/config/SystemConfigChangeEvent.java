package com.qxcmp.config;

import com.qxcmp.core.event.QxcmpEvent;
import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统配置更新事件
 *
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class SystemConfigChangeEvent implements QxcmpEvent {

    /**
     * 系统配置名称空间，系统配置所在类的全名
     */
    private final String namespace;
    private final User user;
    private final String name;
    private final String previous;
    private final String current;

}
