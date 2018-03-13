package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminProfileSecurityEmailBindForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_PROFILE_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminProfileSecurityEmailBindPage extends AbstractQxcmpAdminFormPage<AdminProfileSecurityEmailBindForm> {
    public AdminProfileSecurityEmailBindPage(AdminProfileSecurityEmailBindForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("个人中心", ADMIN_PROFILE_URL, "安全设置", ADMIN_PROFILE_URL + "/security", "邮箱绑定");
    }
}
