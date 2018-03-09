package com.qxcmp.admin.page;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.VERTICAL_MENU_ADMIN_SETTINGS;

/**
 * @author Aaric
 */
public class AdminSettingsPage extends AbstractQxcmpAdminPage {
    @Override
    public void render() {
        setVerticalMenu(VERTICAL_MENU_ADMIN_SETTINGS, "");
        addComponent(viewHelper.nextOverview("系统设置"));
    }
}
