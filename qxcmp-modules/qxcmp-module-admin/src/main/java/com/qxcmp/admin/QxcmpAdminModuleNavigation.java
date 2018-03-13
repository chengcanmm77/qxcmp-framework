package com.qxcmp.admin;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import com.qxcmp.web.view.elements.icon.Icon;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModule.*;
import static com.qxcmp.admin.QxcmpAdminModuleSecurity.*;

/**
 * 后台导航配置
 *
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class QxcmpAdminModuleNavigation implements NavigationLoader {

    public static final String NAVIGATION_ADMIN_SIDEBAR = "ADMIN-SIDEBAR";
    public static final String NAVIGATION_ADMIN_SIDEBAR_TOOLS = NAVIGATION_ADMIN_SIDEBAR + "-TOOLS";
    public static final String NAVIGATION_ADMIN_SIDEBAR_SETTINGS = NAVIGATION_ADMIN_SIDEBAR + "-SETTINGS";

    public static final String ADMIN_MENU_TOOLS = "ADMIN-TOOLS";
    public static final String ADMIN_MENU_TOOLS_LOG = ADMIN_MENU_TOOLS + "-LOG";

    public static final String ADMIN_MENU_SETTINGS = "ADMIN-SETTINGS";
    public static final String ADMIN_MENU_SETTINGS_SITE = ADMIN_MENU_SETTINGS + "-SITE";
    public static final String ADMIN_MENU_SETTINGS_DICTIONARY = ADMIN_MENU_SETTINGS + "-DICTIONARY";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR, "后台侧边导航栏")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_TOOLS, "系统工具", ADMIN_TOOLS_URL).setIcon(new Icon("lab")).setOrder(80).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_TOOL)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_SETTINGS, "设置中心", ADMIN_SETTINGS_URL).setIcon(new Icon("settings")).setOrder(90).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
        );
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS, "系统工具菜单")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS_LOG, "系统日志", ADMIN_AUDIT_LOG_URL).setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_LOG)))
        );
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS, "设置中心菜单")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_SITE, "网站设置", ADMIN_SETTINGS_SITE_URL).setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS_SITE)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_DICTIONARY, "字典设置", ADMIN_SETTINGS_DICTIONARY_URL).setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS_DICTIONARY)))
        );
    }
}
