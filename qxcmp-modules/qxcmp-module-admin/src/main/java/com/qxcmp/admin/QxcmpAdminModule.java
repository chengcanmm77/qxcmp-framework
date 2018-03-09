package com.qxcmp.admin;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.module.ModuleLoaderAdapter;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationService;
import com.qxcmp.web.view.elements.icon.Icon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_SETTINGS;
import static com.qxcmp.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_TOOL;

/**
 * 后台基础模块
 *
 * @author Aaric
 */
@Slf4j
@Component
public class QxcmpAdminModule extends ModuleLoaderAdapter {

    /*
     * 全局常量
     * */

    public static final String ADMIN_URL = "/admin";
    public static final String ADMIN_AUDIT_LOG_URL = ADMIN_URL + "/audit";

    /*
     * 模块权限
     * */

    public static final String PRIVILEGE_SYSTEM_ADMIN = "系统管理员权限";
    public static final String PRIVILEGE_SYSTEM_ADMIN_DESCRIPTION = "可以进入后台系统";

    /*
     * 模块导航
     * */

    public static final String NAVIGATION_ADMIN_SIDEBAR = "ADMIN-SIDEBAR";
    public static final String NAVIGATION_ADMIN_SIDEBAR_TOOLS = NAVIGATION_ADMIN_SIDEBAR + "-TOOLS";
    public static final String NAVIGATION_ADMIN_SIDEBAR_SETTINGS = NAVIGATION_ADMIN_SIDEBAR + "-SETTINGS";

    public static final String VERTICAL_MENU_ADMIN_SETTINGS = "ADMIN-SETTINGS";
    public static final String VERTICAL_MENU_ADMIN_SETTINGS_SITE = VERTICAL_MENU_ADMIN_SETTINGS + "-SITE";
    public static final String VERTICAL_MENU_ADMIN_SETTINGS_DICTIONARY = VERTICAL_MENU_ADMIN_SETTINGS + "-DICTIONARY";
    public static final String VERTICAL_MENU_ADMIN_SETTINGS_REGION = VERTICAL_MENU_ADMIN_SETTINGS + "-REGION";
    public static final String VERTICAL_MENU_ADMIN_SETTINGS_SECURITY = VERTICAL_MENU_ADMIN_SETTINGS + "-SECURITY";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.add(new Navigation(NAVIGATION_ADMIN_SIDEBAR, "后台侧边导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_TOOLS, "系统工具", ADMIN_URL + "/tools").setIcon(new Icon("lab")).setOrder(80).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_TOOL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_SETTINGS, "设置中心", ADMIN_URL + "/settings").setIcon(new Icon("settings")).setOrder(90).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
        );
        navigationService.add(new Navigation(VERTICAL_MENU_ADMIN_SETTINGS, "设置中心菜单")
                .addItem(new Navigation(VERTICAL_MENU_ADMIN_SETTINGS_SITE, "网站设置", QXCMP_ADMIN_URL + "/settings/site").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
                .addItem(new Navigation(VERTICAL_MENU_ADMIN_SETTINGS_DICTIONARY, "字典设置", QXCMP_ADMIN_URL + "/settings/dictionary").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
        );
    }
}
