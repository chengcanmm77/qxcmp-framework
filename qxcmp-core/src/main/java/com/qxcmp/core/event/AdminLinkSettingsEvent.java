package com.qxcmp.core.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminLinkSettingsEvent {

    private final User target;

    public AdminLinkSettingsEvent(User target) {
        this.target = target;
    }
}
