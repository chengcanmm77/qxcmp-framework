package com.qxcmp.core.event;

import com.qxcmp.spdier.SpiderRuntime;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSpiderStopEvent {

    private final User user;
    private final SpiderRuntime spiderRuntime;

    public AdminSpiderStopEvent(User user, SpiderRuntime spiderRuntime) {
        this.user = user;
        this.spiderRuntime = spiderRuntime;
    }
}
