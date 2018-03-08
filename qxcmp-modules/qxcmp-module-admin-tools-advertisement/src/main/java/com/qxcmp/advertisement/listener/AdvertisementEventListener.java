package com.qxcmp.advertisement.listener;

import com.qxcmp.advertisement.Advertisement;
import com.qxcmp.config.SiteService;
import com.qxcmp.core.entity.EntityCreateEvent;
import com.qxcmp.core.entity.EntityDeleteEvent;
import com.qxcmp.core.entity.EntityUpdateEvent;
import com.qxcmp.message.MessageService;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.qxcmp.advertisement.AdvertisementModule.ADMIN_ADVERTISEMENT_URL;
import static com.qxcmp.advertisement.AdvertisementModule.PRIVILEGE_ADMIN_ADVERTISEMENT;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdvertisementEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onNewEvent(EntityCreateEvent<Advertisement> event) {
        Advertisement advertisement = event.getEntity();
        messageService.feedForUsers(PRIVILEGE_ADMIN_ADVERTISEMENT, event.getUser(), feed -> {
            feed.setContent(String.format("%s 新建了一条广告 <a href='https://%s'>%s</a>",
                    event.getUser().getDisplayName(),
                    siteService.getDomain() + ADMIN_ADVERTISEMENT_URL + "/" + advertisement.getId() + "/edit",
                    StringUtils.isBlank(advertisement.getTitle()) ? "无广告名称" : advertisement.getTitle()));
            feed.setExtraContent(String.format("<a href='https://%s'>广告管理</a>", siteService.getDomain() + ADMIN_ADVERTISEMENT_URL));
        });
    }

    @EventListener
    public void onEditEvent(EntityUpdateEvent<Advertisement> event) {
        Advertisement advertisement = event.getEntity();
        messageService.feedForUsers(PRIVILEGE_ADMIN_ADVERTISEMENT, event.getUser(), feed -> {
            feed.setContent(String.format("%s 编辑了一条广告 <a href='https://%s'>%s</a>",
                    event.getUser().getDisplayName(),
                    siteService.getDomain() + ADMIN_ADVERTISEMENT_URL + "/" + advertisement.getId() + "/edit",
                    StringUtils.isBlank(advertisement.getTitle()) ? "无广告名称" : advertisement.getTitle()));
            feed.setExtraContent(String.format("<a href='https://%s'>广告管理</a>", siteService.getDomain() + ADMIN_ADVERTISEMENT_URL));
        });
    }

    @EventListener
    public void onDeleteEvent(EntityDeleteEvent<Advertisement> event) {
        Advertisement advertisement = event.getEntity();
        messageService.feedForUsers(PRIVILEGE_ADMIN_ADVERTISEMENT, event.getUser(), feed -> {
            feed.setContent(String.format("%s 删除了一条广告 %s",
                    event.getUser().getDisplayName(),
                    StringUtils.isBlank(advertisement.getTitle()) ? "无广告名称" : advertisement.getTitle()));
            feed.setExtraContent(String.format("<a href='https://%s'>广告管理</a>", siteService.getDomain() + ADMIN_ADVERTISEMENT_URL));
        });
    }
}
