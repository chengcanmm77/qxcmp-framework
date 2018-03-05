package com.qxcmp.core.event;

import com.qxcmp.spdier.SpiderDefinition;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSpiderDisableEvent {

    private final User user;
    private final SpiderDefinition spiderDefinition;

    public AdminSpiderDisableEvent(User user, SpiderDefinition spiderDefinition) {
        this.user = user;
        this.spiderDefinition = spiderDefinition;
    }
}
