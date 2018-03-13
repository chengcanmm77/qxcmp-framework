package com.qxcmp.admin.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSecurityAuthenticationEvent {

    private final User target;

    public AdminSecurityAuthenticationEvent(User target) {
        this.target = target;
    }
}
