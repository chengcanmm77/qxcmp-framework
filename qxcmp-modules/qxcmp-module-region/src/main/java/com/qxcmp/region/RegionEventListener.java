package com.qxcmp.region;

import com.qxcmp.config.SiteService;
import com.qxcmp.core.entity.EntityCreateEvent;
import com.qxcmp.message.FeedService;
import com.qxcmp.region.event.RegionDisableEvent;
import com.qxcmp.region.event.RegionEnableEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.qxcmp.region.RegionModule.ADMIN_REGION_URL;
import static com.qxcmp.region.RegionModuleSecurity.PRIVILEGE_ADMIN_REGION;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class RegionEventListener {

    private final FeedService feedService;
    private final SiteService siteService;

    @EventListener
    public void onNewEvent(EntityCreateEvent<Region> event) {
        Region region = event.getEntity();
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_REGION, event.getUser(),
                String.format("%s 添加在了地区 <a href='https://%s'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain() + ADMIN_REGION_URL,
                        region.getName() + "-" + region.getCode()));
    }

    @EventListener
    public void onDisableEvent(RegionDisableEvent event) {
        Region region = event.getRegion();
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_REGION, event.getUser(),
                String.format("%s 禁用了地区 <a href='https://%s'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain() + ADMIN_REGION_URL,
                        region.getName() + "-" + region.getCode()));
    }

    @EventListener
    public void onEnableEvent(RegionEnableEvent event) {
        Region region = event.getRegion();
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_REGION, event.getUser(), String.format("%s 启用了地区 <a href='https://%s'>%s</a>",
                event.getUser().getDisplayName(),
                siteService.getDomain() + ADMIN_REGION_URL,
                region.getName() + "-" + region.getCode()));
    }
}
