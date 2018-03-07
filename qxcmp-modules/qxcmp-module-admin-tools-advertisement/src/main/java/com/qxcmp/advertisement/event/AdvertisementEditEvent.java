package com.qxcmp.advertisement.event;

import com.qxcmp.advertisement.Advertisement;
import com.qxcmp.user.User;
import lombok.Data;

/**
 * @author Aaric
 */
@Data
public class AdvertisementEditEvent {

    private final User user;
    private final Advertisement advertisement;

    public AdvertisementEditEvent(User user, Advertisement advertisement) {
        this.user = user;
        this.advertisement = advertisement;
    }
}
