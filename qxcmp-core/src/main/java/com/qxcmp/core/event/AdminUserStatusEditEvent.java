package com.qxcmp.core.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminUserStatusEditEvent {

    private final User source;
    private final User target;

    public AdminUserStatusEditEvent(User source, User target) {
        this.source = source;
        this.target = target;
    }
}
