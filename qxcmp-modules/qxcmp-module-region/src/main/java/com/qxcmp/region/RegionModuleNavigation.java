package com.qxcmp.region;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS;
import static com.qxcmp.region.RegionModule.ADMIN_REGION_URL;
import static com.qxcmp.region.RegionModuleSecurity.PRIVILEGE_ADMIN_REGION;

/**
 * @author Aaric
 */
@Component
public class RegionModuleNavigation implements NavigationLoader {

    public static final String ADMIN_MENU_TOOL_REGION = ADMIN_MENU_TOOLS + "-REGION";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.get(ADMIN_MENU_TOOLS)
                .addItem(new Navigation(ADMIN_MENU_TOOL_REGION, "地区管理", ADMIN_REGION_URL).setOrder(300).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_REGION))
                );
    }
}
