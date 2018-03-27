package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.form.AdminSmsSendForm;
import com.qxcmp.web.view.page.Page;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SMS_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SMS;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SMS_SEND;

/**
 * @author Aaric
 */
@Page
public class AdminSmsSendPage extends AbstractQxcmpAdminFormPage<AdminSmsSendForm> {

    public AdminSmsSendPage(AdminSmsSendForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SMS, ADMIN_MENU_SMS_SEND);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("短信服务", ADMIN_SMS_URL, "短信发送");
    }
}
