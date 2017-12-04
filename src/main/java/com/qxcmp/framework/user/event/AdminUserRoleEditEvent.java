package com.qxcmp.framework.user.event;

import com.qxcmp.framework.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminUserRoleEditEvent {

    private final User source;
    private final User target;

    public AdminUserRoleEditEvent(User source, User target) {
        this.source = source;
        this.target = target;
    }
}
