package com.qxcmp.core.event;

import com.qxcmp.config.SystemDictionary;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSettingsDictionaryEvent {

    private final User user;
    private final SystemDictionary systemDictionary;

    public AdminSettingsDictionaryEvent(User user, SystemDictionary systemDictionary) {
        this.user = user;
        this.systemDictionary = systemDictionary;
    }
}
