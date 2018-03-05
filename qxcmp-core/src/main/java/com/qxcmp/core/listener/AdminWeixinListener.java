package com.qxcmp.core.listener;

import com.qxcmp.config.SiteService;
import com.qxcmp.core.event.AdminWeixinMaterialSyncFinishEvent;
import com.qxcmp.core.event.AdminWeixinMaterialSyncStartEvent;
import com.qxcmp.core.event.AdminWeixinMenuEvent;
import com.qxcmp.core.event.AdminWeixinSettingsEvent;
import com.qxcmp.message.MessageService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.weixin.event.WeixinUserSyncFinishEvent;
import com.qxcmp.weixin.event.WeixinUserSyncStartEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpSecurityConfiguration.*;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminWeixinListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onMaterialSyncEvent(AdminWeixinMaterialSyncStartEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_WEIXIN_MATERIAL);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 触发了 <a href='https://%s/admin/weixin/material'>微信素材同步</a>", target.getDisplayName(), siteService.getDomain()));
    }

    @EventListener
    public void onMaterialSyncFinishEvent(AdminWeixinMaterialSyncFinishEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_WEIXIN_MATERIAL);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 触发的 <a href='https://%s/admin/weixin/material'>微信素材同步</a> 已完成", target.getDisplayName(), siteService.getDomain()),
                "此次同步素材总数：" + event.getTotalCount());
    }

    @EventListener
    public void onUserSyncEvent(WeixinUserSyncStartEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_USER);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 触发了 <a href='https://%s/admin/user/weixin'>微信用户同步</a>", target.getDisplayName(), siteService.getDomain()));
    }

    @EventListener
    public void onUserSyncFinishEvent(WeixinUserSyncFinishEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_USER);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 触发的 <a href='https://%s/admin/user/weixin'>微信用户同步</a> 已完成", target.getDisplayName(), siteService.getDomain()),
                "此次同步用户总数：" + event.getTotalUser());
    }

    @EventListener
    public void onSettingEvent(AdminWeixinSettingsEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_WEIXIN_SETTINGS);
        feedUsers.add(target);
        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 修改了 <a href='https://%s/admin/weixin/settings'>微信公众号配置</a>", target.getDisplayName(), siteService.getDomain()));

    }

    @EventListener
    public void onMenuEvent(AdminWeixinMenuEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_WEIXIN_MENU);
        feedUsers.add(target);
        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 修改了 <a href='https://%s/admin/weixin/menu'>微信公众号菜单</a>", target.getDisplayName(), siteService.getDomain()));

    }
}
