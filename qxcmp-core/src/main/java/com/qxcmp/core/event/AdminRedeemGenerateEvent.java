package com.qxcmp.core.event;

import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminRedeemGenerateEvent {

    private final User target;
    private final String name;
    private final int count;

    public AdminRedeemGenerateEvent(User target, String name, int count) {
        this.target = target;
        this.name = name;
        this.count = count;
    }
}
