package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminSettingsSmsForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SETTINGS_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_SMS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminSettingsSmsPage extends AbstractQxcmpAdminFormPage<AdminSettingsSmsForm> {

    public AdminSettingsSmsPage(AdminSettingsSmsForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SETTINGS, ADMIN_MENU_SETTINGS_SMS);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("设置中心", ADMIN_SETTINGS_URL, "短信服务设置");
    }
}
