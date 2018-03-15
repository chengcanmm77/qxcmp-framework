package com.qxcmp.weixin.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminWeixinMaterialSyncStartEvent {

    private final User target;

    public AdminWeixinMaterialSyncStartEvent(User target) {
        this.target = target;
    }
}
