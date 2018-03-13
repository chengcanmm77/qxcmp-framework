package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminSecurityRoleEditForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SECURITY_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_ROLE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminSecurityRoleEditPage extends AbstractQxcmpAdminFormPage<AdminSecurityRoleEditForm> {
    public AdminSecurityRoleEditPage(AdminSecurityRoleEditForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SECURITY, ADMIN_MENU_SECURITY_ROLE);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("安全配置", ADMIN_SECURITY_URL, "角色管理", ADMIN_SECURITY_URL + "/role", "编辑角色");
    }
}
