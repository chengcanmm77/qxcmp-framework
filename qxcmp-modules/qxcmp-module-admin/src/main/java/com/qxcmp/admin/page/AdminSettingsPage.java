package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.VERTICAL_MENU_ADMIN_SETTINGS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminSettingsPage extends AbstractQxcmpAdminPage {
    @Override
    public void render() {
        setMenu(VERTICAL_MENU_ADMIN_SETTINGS, "");
        addComponent(viewHelper.nextOverview("设置中心"));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("设置中心");
    }
}
