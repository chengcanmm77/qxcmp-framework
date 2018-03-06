package com.qxcmp.account.event;

import com.qxcmp.core.event.QxcmpEvent;
import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户登录
 *
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class UserLoginEvent implements QxcmpEvent {

    /**
     * 登录成功的用户
     */
    private final User user;
}
