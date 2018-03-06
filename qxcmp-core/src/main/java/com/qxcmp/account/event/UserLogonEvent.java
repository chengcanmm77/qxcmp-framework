package com.qxcmp.account.event;

import com.qxcmp.core.event.QxcmpEvent;
import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户注册账户事件
 *
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class UserLogonEvent implements QxcmpEvent {

    /**
     * 注册的用户
     */
    private final User user;
}
