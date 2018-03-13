package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminSettingsSiteForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SETTINGS_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_SITE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminSettingsSitePage extends AbstractQxcmpAdminFormPage<AdminSettingsSiteForm> {

    public AdminSettingsSitePage(AdminSettingsSiteForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SETTINGS, ADMIN_MENU_SETTINGS_SITE);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("设置中心", ADMIN_SETTINGS_URL, "网站设置");
    }
}
