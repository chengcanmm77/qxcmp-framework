package com.qxcmp.advertisement;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS;
import static com.qxcmp.advertisement.AdvertisementModule.ADMIN_ADVERTISEMENT_URL;
import static com.qxcmp.advertisement.AdvertisementModuleSecurity.PRIVILEGE_ADMIN_ADVERTISEMENT;

/**
 * @author Aaric
 */
@Component
public class AdvertisementModuleNavigation implements NavigationLoader {

    public static final String ADMIN_MENU_TOOLS_ADVERTISEMENT = ADMIN_MENU_TOOLS + "-ADVERTISEMENT";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.get(ADMIN_MENU_TOOLS)
                .addItem(new Navigation(ADMIN_MENU_TOOLS_ADVERTISEMENT, "广告管理", ADMIN_ADVERTISEMENT_URL).setOrder(100).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_ADVERTISEMENT))
                );
    }
}
