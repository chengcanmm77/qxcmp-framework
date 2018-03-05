package com.qxcmp.core.event;

import com.qxcmp.region.Region;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminSettingsRegionEvent {

    private final User user;
    private final Region region;
    private final String action;

    public AdminSettingsRegionEvent(User user, Region region, String action) {
        this.user = user;
        this.region = region;
        this.action = action;
    }
}
