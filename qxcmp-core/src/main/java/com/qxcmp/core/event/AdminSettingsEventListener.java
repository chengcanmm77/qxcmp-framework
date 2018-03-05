package com.qxcmp.core.event;

import com.qxcmp.config.SiteService;
import com.qxcmp.message.MessageService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_SETTINGS;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminSettingsEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onSiteEvent(AdminSettingsSiteEvent event) {
        User target = event.getUser();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SETTINGS);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 修改了 <a href='https://%s/admin/settings/site'>网站配置</a>", target.getDisplayName(), siteService.getDomain()));
    }

    @EventListener
    public void onDictionaryEvent(AdminSettingsDictionaryEvent event) {
        User target = event.getUser();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SETTINGS);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 修改了系统字典 <a href='https://%s/admin/settings/dictionary/%s/edit'>%s</a>"
                        , target.getDisplayName()
                        , siteService.getDomain()
                        , event.getSystemDictionary().getName()
                        , event.getSystemDictionary().getName()));
    }

    @EventListener
    public void onRegionEvent(AdminSettingsRegionEvent event) {
        User target = event.getUser();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SETTINGS);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s %s了区域 <a href='https://%s/admin/settings/region'>%s</a>"
                        , target.getDisplayName()
                        , getRegionAction(event.getAction())
                        , siteService.getDomain()
                        , event.getRegion().getName()));
    }

    private String getRegionAction(String action) {
        switch (action) {
            case "new":
                return "新建";
            case "disable":
                return "禁用";
            case "enable":
                return "启用";
            default:
                return "";
        }
    }
}
