package com.qxcmp.weixin.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class WeixinUserSyncFinishEvent {

    private final User target;
    private final long totalUser;

    public WeixinUserSyncFinishEvent(User target, long totalUser) {
        this.target = target;
        this.totalUser = totalUser;
    }
}
