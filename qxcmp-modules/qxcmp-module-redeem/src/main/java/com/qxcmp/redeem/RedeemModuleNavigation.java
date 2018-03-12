package com.qxcmp.redeem;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS;
import static com.qxcmp.redeem.RedeemModule.ADMIN_REDEEM_URL;
import static com.qxcmp.redeem.RedeemModuleSecurity.PRIVILEGE_ADMIN_REDEEM;
import static com.qxcmp.redeem.RedeemModuleSecurity.PRIVILEGE_ADMIN_REDEEM_SETTING;

/**
 * @author Aaric
 */
@Component
public class RedeemModuleNavigation implements NavigationLoader {

    public static final String ADMIN_MENU_TOOLS_REDEEM = ADMIN_MENU_TOOLS + "-REDEEM";
    public static final String ADMIN_MENU_SETTINGS_REDEEM = ADMIN_MENU_SETTINGS + "-REDEEM";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.get(ADMIN_MENU_TOOLS)
                .addItem(new Navigation(ADMIN_MENU_TOOLS_REDEEM, "兑换码管理", ADMIN_REDEEM_URL).setOrder(400).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_REDEEM))
                );
        navigationService.get(ADMIN_MENU_SETTINGS)
                .addItem(new Navigation(ADMIN_MENU_SETTINGS_REDEEM, "兑换码设置", ADMIN_REDEEM_URL + "/settings").setOrder(400).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_REDEEM_SETTING))
                );
    }
}
