package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminSecurityAuthenticationForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SECURITY_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_AUTHENTICATION;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminSecurityAuthenticationPage extends AbstractQxcmpAdminFormPage<AdminSecurityAuthenticationForm> {
    public AdminSecurityAuthenticationPage(AdminSecurityAuthenticationForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SECURITY, ADMIN_MENU_SECURITY_AUTHENTICATION);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("安全配置", ADMIN_SECURITY_URL, "认证配置");
    }
}
