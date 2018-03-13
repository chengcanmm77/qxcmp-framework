package com.qxcmp.admin.listener;

import com.qxcmp.admin.event.*;
import com.qxcmp.config.SiteService;
import com.qxcmp.message.MessageService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.admin.QxcmpAdminModuleSecurity.PRIVILEGE_ADMIN_SECURITY;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminSecurityEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onAuthenticationEvent(AdminSecurityAuthenticationEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SECURITY);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 修改了 <a href='https://%s/admin/security/authentication'>安全认证配置</a>", target.getDisplayName(), siteService.getDomain()));
    }

    @EventListener
    public void onPrivilegeDisableEvent(AdminSecurityPrivilegeDisableEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SECURITY);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 禁用了权限 <a href='https://%s/admin/security/privilege'>%s</a>", target.getDisplayName(), siteService.getDomain(), event.getPrivilege().getName()));
    }

    @EventListener
    public void onPrivilegeEnableEvent(AdminSecurityPrivilegeEnableEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SECURITY);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 启用了权限 <a href='https://%s/admin/security/privilege'>%s</a>", target.getDisplayName(), siteService.getDomain(), event.getPrivilege().getName()));
    }

    @EventListener
    public void onRoleNewEvent(AdminSecurityRoleNewEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SECURITY);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 新建了角色 <a href='https://%s/admin/security/role/%d/edit'>%s</a>", target.getDisplayName(), siteService.getDomain(), event.getRole().getId(), event.getRole().getName()));
    }

    @EventListener
    public void onRoleEditEvent(AdminSecurityRoleEditEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_SECURITY);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 编辑了角色 <a href='https://%s/admin/security/role/%d/edit'>%s</a>", target.getDisplayName(), siteService.getDomain(), event.getRole().getId(), event.getRole().getName()));
    }
}
