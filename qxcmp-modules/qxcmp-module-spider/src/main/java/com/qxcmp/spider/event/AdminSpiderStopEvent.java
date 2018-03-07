package com.qxcmp.spider.event;

import com.qxcmp.spider.SpiderRuntime;
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
