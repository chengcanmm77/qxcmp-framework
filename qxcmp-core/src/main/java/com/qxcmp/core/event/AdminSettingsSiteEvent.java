package com.qxcmp.core.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSettingsSiteEvent {

    private final User user;

    public AdminSettingsSiteEvent(User user) {
        this.user = user;
    }
}
