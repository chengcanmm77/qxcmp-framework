package com.qxcmp.weixin.listener;

import com.qxcmp.config.SiteService;
import com.qxcmp.config.SystemConfigChangeEvent;
import com.qxcmp.message.FeedService;
import com.qxcmp.weixin.WeixinModuleSystemConfig;
import com.qxcmp.weixin.event.AdminWeixinMenuEvent;
import com.qxcmp.weixin.event.WeixinUserSyncFinishEvent;
import com.qxcmp.weixin.event.WeixinUserSyncStartEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.qxcmp.weixin.WeixinModuleSecurity.PRIVILEGE_ADMIN_WEIXIN;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminWeixinListener {

    private final FeedService feedService;
    private final SiteService siteService;

    @EventListener
    public void onUserSyncEvent(WeixinUserSyncStartEvent event) {
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_WEIXIN, event.getTarget(),
                String.format("%s 触发了 <a href='https://%s/admin/user/weixin'>微信用户同步</a>", event.getTarget().getDisplayName(), siteService.getDomain()));
    }

    @EventListener
    public void onUserSyncFinishEvent(WeixinUserSyncFinishEvent event) {
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_WEIXIN, event.getTarget(),
                String.format("%s 触发的 <a href='https://%s/admin/user/weixin'>微信用户同步</a> 已完成", event.getTarget().getDisplayName(), siteService.getDomain()),
                "此次同步用户总数：" + event.getTotalUser());
    }

    @EventListener
    public void onSettingEvent(SystemConfigChangeEvent event) {
        if (StringUtils.equals(event.getNamespace(), WeixinModuleSystemConfig.class.getName())) {
            feedService.feedForUserGroup(PRIVILEGE_ADMIN_WEIXIN, event.getUser(),
                    String.format("%s 修改了 <a href='https:///%s/admin/weixin'>微信平台参数</a>",
                            event.getUser().getDisplayName(),
                            siteService.getDomain()),
                    String.format("[%s]从[%s]修改为[%s]", event.getName(), event.getPrevious(), event.getCurrent()));
        }
    }

    @EventListener
    public void onMenuEvent(AdminWeixinMenuEvent event) {
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_WEIXIN, event.getTarget(),
                String.format("%s 修改了 <a href='https://%s/admin/weixin/menu'>公众号菜单</a>", event.getTarget().getDisplayName(), siteService.getDomain()));

    }
}
