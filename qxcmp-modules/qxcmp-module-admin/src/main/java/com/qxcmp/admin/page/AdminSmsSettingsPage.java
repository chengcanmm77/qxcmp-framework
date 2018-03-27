package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminSmsSettingsForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SMS_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SMS;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SMS_SETTINGS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminSmsSettingsPage extends AbstractQxcmpAdminFormPage<AdminSmsSettingsForm> {

    public AdminSmsSettingsPage(AdminSmsSettingsForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SMS, ADMIN_MENU_SMS_SETTINGS);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("短信服务", ADMIN_SMS_URL, "短信服务设置");
    }
}
