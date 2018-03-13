package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminSettingsEmailForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SETTINGS_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_EMAIL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminSettingsEmailPage extends AbstractQxcmpAdminFormPage<AdminSettingsEmailForm> {

    public AdminSettingsEmailPage(AdminSettingsEmailForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SETTINGS, ADMIN_MENU_SETTINGS_EMAIL);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("设置中心", ADMIN_SETTINGS_URL, "邮件服务设置");
    }
}
