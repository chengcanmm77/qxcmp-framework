package com.qxcmp.core.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminWeixinMaterialSyncFinishEvent {

    private final User target;
    private final long totalCount;

    public AdminWeixinMaterialSyncFinishEvent(User target, long totalCount) {
        this.target = target;
        this.totalCount = totalCount;
    }
}
