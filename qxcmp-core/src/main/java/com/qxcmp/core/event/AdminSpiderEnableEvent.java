package com.qxcmp.core.event;

import com.qxcmp.spdier.SpiderDefinition;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSpiderEnableEvent {

    private final User user;
    private final SpiderDefinition spiderDefinition;

    public AdminSpiderEnableEvent(User user, SpiderDefinition spiderDefinition) {
        this.user = user;
        this.spiderDefinition = spiderDefinition;
    }
}
