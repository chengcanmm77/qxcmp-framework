package com.qxcmp.config;

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
public class SystemConfigChangeEvent {

    private final User user;
    private final String name;
    private final String previous;
    private final String current;
}
