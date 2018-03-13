package com.qxcmp.link.listener;

import com.qxcmp.config.SiteService;
import com.qxcmp.core.entity.EntityCreateEvent;
import com.qxcmp.core.entity.EntityDeleteEvent;
import com.qxcmp.core.entity.EntityUpdateEvent;
import com.qxcmp.link.Link;
import com.qxcmp.message.FeedService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.qxcmp.link.LinkModule.ADMIN_LINK_URL;
import static com.qxcmp.link.LinkModuleSecurity.PRIVILEGE_ADMIN_LINK;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class LinkEventListener {

    private final FeedService feedService;
    private final SiteService siteService;

    @EventListener
    public void onNewEvent(EntityCreateEvent<Link> event) {
        Link link = event.getEntity();
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_LINK, event.getUser(),
                String.format("%s 新建了一条链接 <a href='https://%s'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain() + ADMIN_LINK_URL + "/" + link.getId() + "/edit",
                        StringUtils.isBlank(link.getTitle()) ? "无链接名称" : link.getTitle()),
                String.format("<a href='https://%s'>链接管理</a>", siteService.getDomain() + ADMIN_LINK_URL));
    }

    @EventListener
    public void onEditEvent(EntityUpdateEvent<Link> event) {
        Link link = event.getEntity();
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_LINK, event.getUser(),
                String.format("%s 编辑了一条链接 <a href='https://%s'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain() + ADMIN_LINK_URL + "/" + link.getId() + "/edit",
                        StringUtils.isBlank(link.getTitle()) ? "无链接名称" : link.getTitle()),
                String.format("<a href='https://%s'>链接管理</a>", siteService.getDomain() + ADMIN_LINK_URL));
    }

    @EventListener
    public void onDeleteEvent(EntityDeleteEvent<Link> event) {
        Link link = event.getEntity();
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_LINK, event.getUser(),
                String.format("%s 删除了一条链接 %s",
                        event.getUser().getDisplayName(),
                        StringUtils.isBlank(link.getTitle()) ? "无链接名称" : link.getTitle()),
                String.format("<a href='https://%s'>链接管理</a>", siteService.getDomain() + ADMIN_LINK_URL));
    }
}
