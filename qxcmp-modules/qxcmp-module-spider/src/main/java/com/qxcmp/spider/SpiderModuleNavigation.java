package com.qxcmp.spider;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS;
import static com.qxcmp.spider.SpiderModule.ADMIN_SPIDER_URL;
import static com.qxcmp.spider.SpiderModuleSecurity.PRIVILEGE_ADMIN_SPIDER;

/**
 * @author Aaric
 */
@Component
public class SpiderModuleNavigation implements NavigationLoader {

    public static final String ADMIN_MENU_TOOLS_SPIDER = ADMIN_MENU_TOOLS + "-SPIDER";

    public static final String ADMIN_MENU_SPIDER = "ADMIN-SPIDER";
    public static final String ADMIN_MENU_SPIDER_STATUS = ADMIN_MENU_SPIDER + "-TABLE";
    public static final String ADMIN_MENU_SPIDER_LOG = ADMIN_MENU_SPIDER + "-LOG";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.get(ADMIN_MENU_TOOLS)
                .addItem(new Navigation(ADMIN_MENU_TOOLS_SPIDER, "蜘蛛管理", ADMIN_SPIDER_URL).setOrder(300).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SPIDER))
                );
        navigationService.add(new Navigation(ADMIN_MENU_SPIDER, "蜘蛛管理菜单")
                .addItem(new Navigation(ADMIN_MENU_SPIDER_STATUS, "运行状态", ADMIN_SPIDER_URL + "/status").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SPIDER)))
                .addItem(new Navigation(ADMIN_MENU_SPIDER_LOG, "抓取日志", ADMIN_SPIDER_URL + "/log").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SPIDER)))
        );
    }
}
