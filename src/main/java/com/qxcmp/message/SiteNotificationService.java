package com.qxcmp.message;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Aaric
 */
@Service
public class SiteNotificationService extends AbstractEntityService<SiteNotification, Long, SiteNotificationRepository> {

    public Optional<SiteNotification> findOne(String id) {
        try {
            return findOne(Long.parseLong(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 当前启用的网站通知
     *
     * @return 当前激活的网站通知中的第一个
     */
    public Optional<SiteNotification> findActiveNotifications() {
        return repository.findActive().stream().findAny();
    }

}
