package com.qxcmp.admin;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import com.qxcmp.web.view.elements.icon.Icon;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_SETTINGS;
import static com.qxcmp.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_TOOL;

/**
 * 后台导航配置
 *
 * @author Aaric
 */
@Component
public class QxcmpAdminModuleNavigation implements NavigationLoader {

    public static final String NAVIGATION_ADMIN_SIDEBAR = "ADMIN-SIDEBAR";
    public static final String NAVIGATION_ADMIN_SIDEBAR_TOOLS = NAVIGATION_ADMIN_SIDEBAR + "-TOOLS";
    public static final String NAVIGATION_ADMIN_SIDEBAR_SETTINGS = NAVIGATION_ADMIN_SIDEBAR + "-SETTINGS";

    public static final String VERTICAL_MENU_ADMIN_SETTINGS = "ADMIN-SETTINGS";
    public static final String VERTICAL_MENU_ADMIN_SETTINGS_SITE = VERTICAL_MENU_ADMIN_SETTINGS + "-SITE";
    public static final String VERTICAL_MENU_ADMIN_SETTINGS_DICTIONARY = VERTICAL_MENU_ADMIN_SETTINGS + "-DICTIONARY";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR, "后台侧边导航栏")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_TOOLS, "系统工具", ADMIN_URL + "/tools").setIcon(new Icon("lab")).setOrder(80).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_TOOL)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_SETTINGS, "设置中心", ADMIN_URL + "/settings").setIcon(new Icon("settings")).setOrder(90).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
        );
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.VERTICAL_MENU_ADMIN_SETTINGS, "设置中心菜单")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.VERTICAL_MENU_ADMIN_SETTINGS_SITE, "网站设置", QXCMP_ADMIN_URL + "/settings/site").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.VERTICAL_MENU_ADMIN_SETTINGS_DICTIONARY, "字典设置", QXCMP_ADMIN_URL + "/settings/dictionary").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
        );
    }
}
