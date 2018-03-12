package com.qxcmp.region.event;

import com.qxcmp.region.Region;
import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class RegionDisableEvent {

    private final User user;
    private final Region region;

}
