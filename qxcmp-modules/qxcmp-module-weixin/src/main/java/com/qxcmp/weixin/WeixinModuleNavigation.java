package com.qxcmp.weixin;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import com.qxcmp.web.view.elements.icon.Icon;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR;
import static com.qxcmp.weixin.WeixinModule.ADMIN_WEIXIN_URL;
import static com.qxcmp.weixin.WeixinModuleSecurity.PRIVILEGE_ADMIN_WEIXIN;

/**
 * @author Aaric
 */
@Component
public class WeixinModuleNavigation implements NavigationLoader {

    public static final String NAVIGATION_ADMIN_SIDEBAR_WEIXIN = NAVIGATION_ADMIN_SIDEBAR + "-WEIXIN";

    public static final String ADMIN_MENU_WEIXIN = "ADMIN-WEIXIN";
    public static final String ADMIN_MENU_WEIXIN_SETTINGS = ADMIN_MENU_WEIXIN + "-SETTINGS";
    public static final String ADMIN_MENU_WEIXIN_MENU = ADMIN_MENU_WEIXIN + "-MENU";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.get(NAVIGATION_ADMIN_SIDEBAR)
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_WEIXIN, "微信平台", ADMIN_WEIXIN_URL).setIcon(new Icon("wechat")).setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_WEIXIN)));
        navigationService.add(new Navigation(ADMIN_MENU_WEIXIN, "微信平台设置导航栏")
                .addItem(new Navigation(ADMIN_MENU_WEIXIN_SETTINGS, "公众号设置", ADMIN_WEIXIN_URL + "/settings").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_WEIXIN)))
                .addItem(new Navigation(ADMIN_MENU_WEIXIN_MENU, "公众号菜单", ADMIN_WEIXIN_URL + "/menu").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_WEIXIN)))
        );
    }
}
