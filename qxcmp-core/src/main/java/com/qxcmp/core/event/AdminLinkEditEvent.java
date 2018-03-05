package com.qxcmp.core.event;

import com.qxcmp.link.Link;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminLinkEditEvent {

    private final User target;
    private final Link link;

    public AdminLinkEditEvent(User target, Link link) {
        this.target = target;
        this.link = link;
    }
}
