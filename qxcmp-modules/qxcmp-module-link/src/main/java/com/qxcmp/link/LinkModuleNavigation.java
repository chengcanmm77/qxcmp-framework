package com.qxcmp.link;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS;
import static com.qxcmp.link.LinkModule.ADMIN_LINK_URL;
import static com.qxcmp.link.LinkModuleSecurity.PRIVILEGE_ADMIN_LINK;

/**
 * @author Aaric
 */
@Component
public class LinkModuleNavigation implements NavigationLoader {

    public static final String ADMIN_MENU_TOOLS_LINK = ADMIN_MENU_TOOLS + "-LINK";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.get(ADMIN_MENU_TOOLS)
                .addItem(new Navigation(ADMIN_MENU_TOOLS_LINK, "链接管理", ADMIN_LINK_URL).setOrder(200).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_LINK))
                );
    }
}
