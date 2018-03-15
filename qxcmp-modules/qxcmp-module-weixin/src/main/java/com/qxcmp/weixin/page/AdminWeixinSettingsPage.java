package com.qxcmp.weixin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.weixin.form.AdminWeixinSettingsForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.weixin.WeixinModule.ADMIN_WEIXIN_URL;
import static com.qxcmp.weixin.WeixinModuleNavigation.ADMIN_MENU_WEIXIN;
import static com.qxcmp.weixin.WeixinModuleNavigation.ADMIN_MENU_WEIXIN_SETTINGS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminWeixinSettingsPage extends AbstractQxcmpAdminFormPage<AdminWeixinSettingsForm> {
    public AdminWeixinSettingsPage(AdminWeixinSettingsForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_WEIXIN, ADMIN_MENU_WEIXIN_SETTINGS);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("微信平台", ADMIN_WEIXIN_URL, "微信平台参数");
    }
}
