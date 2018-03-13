package com.qxcmp.admin.event;

import com.qxcmp.security.Privilege;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSecurityPrivilegeDisableEvent {

    private final User target;
    private final Privilege privilege;

    public AdminSecurityPrivilegeDisableEvent(User target, Privilege privilege) {
        this.target = target;
        this.privilege = privilege;
    }
}
