package com.qxcmp.spider.listener;

import com.qxcmp.config.SiteService;
import com.qxcmp.message.MessageService;
import com.qxcmp.spider.SpiderDefinition;
import com.qxcmp.spider.SpiderRuntime;
import com.qxcmp.spider.event.AdminSpiderDisableEvent;
import com.qxcmp.spider.event.AdminSpiderEnableEvent;
import com.qxcmp.spider.event.AdminSpiderStopEvent;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_SPIDER;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminSpiderEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onDisableEvent(AdminSpiderDisableEvent event) {
        SpiderDefinition spiderDefinition = event.getSpiderDefinition();
        User user = event.getUser();

        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SPIDER);
        feedUsers.add(user);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), user,
                String.format("%s 禁用了蜘蛛 <a href='https://%s/admin/spider'>%s</a>",
                        user.getDisplayName(),
                        siteService.getDomain(),
                        spiderDefinition.getName()));
    }

    @EventListener
    public void onEnableEvent(AdminSpiderEnableEvent event) {
        SpiderDefinition spiderDefinition = event.getSpiderDefinition();
        User user = event.getUser();

        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SPIDER);
        feedUsers.add(user);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), user,
                String.format("%s 启用了蜘蛛 <a href='https://%s/admin/spider'>%s</a>",
                        user.getDisplayName(),
                        siteService.getDomain(),
                        spiderDefinition.getName()));
    }

    @EventListener
    public void onStopEvent(AdminSpiderStopEvent event) {
        SpiderRuntime spiderRuntime = event.getSpiderRuntime();
        User user = event.getUser();

        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SPIDER);
        feedUsers.add(user);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), user,
                String.format("%s 停止了蜘蛛 <a href='https://%s/admin/spider'>%s</a>",
                        user.getDisplayName(),
                        siteService.getDomain(),
                        spiderRuntime.getName()));
    }
}
