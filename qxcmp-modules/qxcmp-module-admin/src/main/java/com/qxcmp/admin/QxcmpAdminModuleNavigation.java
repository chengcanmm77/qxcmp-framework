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
    public static final String NAVIGATION_ADMIN_SIDEBAR_USER = NAVIGATION_ADMIN_SIDEBAR + "-USER";
    public static final String NAVIGATION_ADMIN_SIDEBAR_FINANCE = NAVIGATION_ADMIN_SIDEBAR + "-FINANCE";
    public static final String NAVIGATION_ADMIN_SIDEBAR_SECURITY = NAVIGATION_ADMIN_SIDEBAR + "-SECURITY";
    public static final String NAVIGATION_ADMIN_SIDEBAR_TOOLS = NAVIGATION_ADMIN_SIDEBAR + "-TOOLS";
    public static final String NAVIGATION_ADMIN_SIDEBAR_SETTINGS = NAVIGATION_ADMIN_SIDEBAR + "-SETTINGS";

    public static final String NAVIGATION_ADMIN_PROFILE = "ADMIN-PROFILE";
    public static final String NAVIGATION_ADMIN_PROFILE_SECURITY = NAVIGATION_ADMIN_PROFILE + "-SECURITY";
    public static final String NAVIGATION_ADMIN_PROFILE_INFO = NAVIGATION_ADMIN_PROFILE + "-INFO";

    public static final String ADMIN_MENU_FINANCE = "ADMIN-FINANCE";
    public static final String ADMIN_MENU_FINANCE_DEPOSIT = ADMIN_MENU_FINANCE + "-DEPOSIT";
    public static final String ADMIN_MENU_FINANCE_WALLET = ADMIN_MENU_FINANCE + "-WALLET";

    public static final String ADMIN_MENU_SECURITY = "ADMIN-SECURITY";
    public static final String ADMIN_MENU_SECURITY_ROLE = ADMIN_MENU_SECURITY + "-ROLE";
    public static final String ADMIN_MENU_SECURITY_PRIVILEGE = ADMIN_MENU_SECURITY + "-PRIVILEGE";
    public static final String ADMIN_MENU_SECURITY_AUTHENTICATION = ADMIN_MENU_SECURITY + "-AUTHENTICATION";

    public static final String ADMIN_MENU_TOOLS = "ADMIN-TOOLS";
    public static final String ADMIN_MENU_TOOLS_LOG = ADMIN_MENU_TOOLS + "-LOG";
    public static final String ADMIN_MENU_TOOLS_STATISTIC = ADMIN_MENU_TOOLS + "-STATISTIC";

    public static final String ADMIN_MENU_SETTINGS = "ADMIN-SETTINGS";
    public static final String ADMIN_MENU_SETTINGS_SITE = ADMIN_MENU_SETTINGS + "-SITE";
    public static final String ADMIN_MENU_SETTINGS_DICTIONARY = ADMIN_MENU_SETTINGS + "-DICTIONARY";
    public static final String ADMIN_MENU_SETTINGS_EMAIL = ADMIN_MENU_SETTINGS + "-EMAIL";
    public static final String ADMIN_MENU_SETTINGS_SMS = ADMIN_MENU_SETTINGS + "-SMS";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR, "后台侧边导航栏")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_USER, "用户管理", ADMIN_USER_URL).setIcon(new Icon("users")).setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_FINANCE, "财务统计", ADMIN_FINANCE_URL).setIcon(new Icon("line chart")).setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_SECURITY, "安全配置", ADMIN_SECURITY_URL).setIcon(new Icon("lock")).setOrder(70).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_TOOLS, "系统工具", ADMIN_TOOLS_URL).setIcon(new Icon("lab")).setOrder(80).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_TOOL)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR_SETTINGS, "设置中心", ADMIN_SETTINGS_URL).setIcon(new Icon("settings")).setOrder(100).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
        );
        navigationService.add(new Navigation(NAVIGATION_ADMIN_PROFILE, "个人中心导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_PROFILE_INFO, "基本资料", ADMIN_PROFILE_URL + "/info").setIcon(new Icon("user")).setOrder(10))
                .addItem(new Navigation(NAVIGATION_ADMIN_PROFILE_SECURITY, "安全设置", ADMIN_PROFILE_URL + "/security").setIcon(new Icon("lock")).setOrder(20))
        );
        navigationService.add(new Navigation(ADMIN_MENU_FINANCE, "财务管理导航栏")
                .addItem(new Navigation(ADMIN_MENU_FINANCE_DEPOSIT, "充值订单", ADMIN_FINANCE_URL + "/deposit").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_DEPOSIT)))
                .addItem(new Navigation(ADMIN_MENU_FINANCE_WALLET, "用户钱包", ADMIN_FINANCE_URL + "/wallet").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_WALLET)))
        );
        navigationService.add(new Navigation(ADMIN_MENU_SECURITY, "安全配置导航栏")
                .addItem(new Navigation(ADMIN_MENU_SECURITY_ROLE, "角色管理", ADMIN_SECURITY_URL + "/role").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
                .addItem(new Navigation(ADMIN_MENU_SECURITY_PRIVILEGE, "权限管理", ADMIN_SECURITY_URL + "/privilege").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
                .addItem(new Navigation(ADMIN_MENU_SECURITY_AUTHENTICATION, "认证配置", ADMIN_SECURITY_URL + "/authentication").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
        );
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS, "系统工具菜单")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS_LOG, "系统日志", ADMIN_AUDIT_LOG_URL).setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_LOG)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS_STATISTIC, "统计信息", ADMIN_STATISTIC_URL).setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_LOG)))
        );
        navigationService.add(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS, "设置中心菜单")
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_SITE, "网站设置", ADMIN_SETTINGS_SITE_URL).setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS_SITE)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_DICTIONARY, "字典设置", ADMIN_SETTINGS_DICTIONARY_URL).setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS_DICTIONARY)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_EMAIL, "邮件服务设置", ADMIN_SETTINGS_EMAIL_URL).setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS_EMAIL)))
                .addItem(new Navigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_SMS, "短信服务设置", ADMIN_SETTINGS_SMS_URL).setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS_SMS)))
        );
    }
}
