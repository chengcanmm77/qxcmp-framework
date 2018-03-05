package com.qxcmp.core.listener;

import com.qxcmp.advertisement.Advertisement;
import com.qxcmp.config.SiteService;
import com.qxcmp.core.event.AdminAdvertisementEditEvent;
import com.qxcmp.core.event.AdminAdvertisementNewEvent;
import com.qxcmp.message.MessageService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_ADVERTISEMENT;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminAdvertisementEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onNewEvent(AdminAdvertisementNewEvent event) {
        Advertisement advertisement = event.getAdvertisement();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_ADVERTISEMENT);
        feedUsers.add(event.getUser());

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), event.getUser(), String.format("%s 新建了一条广告 <a href='https://%s'>%s</a>",
                event.getUser().getDisplayName(),
                siteService.getDomain() + "/admin/advertisement/" + advertisement.getId() + "/edit",
                StringUtils.isNotBlank(advertisement.getTitle()) ? advertisement.getTitle() : "无名称"));
    }

    @EventListener
    public void onEditEvent(AdminAdvertisementEditEvent event) {
        Advertisement advertisement = event.getAdvertisement();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_ADVERTISEMENT);
        feedUsers.add(event.getUser());

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), event.getUser(), String.format("%s 编辑了广告 <a href='https://%s'>%s</a>",
                event.getUser().getDisplayName(),
                siteService.getDomain() + "/admin/advertisement/" + advertisement.getId() + "/edit",
                StringUtils.isNotBlank(advertisement.getTitle()) ? advertisement.getTitle() : "无名称"));
    }
}
