package com.qxcmp.account.event;

import com.qxcmp.core.event.QxcmpEvent;
import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户账户重置事件
 *
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class UserResetEvent implements QxcmpEvent {

    /**
     * 重置以后的用户
     */
    private final User user;
}
