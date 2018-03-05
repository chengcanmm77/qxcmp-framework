package com.qxcmp.core.event;

import com.qxcmp.link.Link;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminLinkNewEvent {

    private final User target;
    private final Link link;

    public AdminLinkNewEvent(User target, Link link) {
        this.target = target;
        this.link = link;
    }
}
