package com.qxcmp.core.event;

import com.qxcmp.advertisement.Advertisement;
import com.qxcmp.user.User;
import lombok.Data;

/**
 * @author Aaric
 */
@Data
public class AdminAdvertisementNewEvent {

    private final User user;
    private final Advertisement advertisement;

    public AdminAdvertisementNewEvent(User user, Advertisement advertisement) {
        this.user = user;
        this.advertisement = advertisement;
    }
}
