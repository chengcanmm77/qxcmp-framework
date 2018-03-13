package com.qxcmp.redeem.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.redeem.form.AdminRedeemSettingsForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS;
import static com.qxcmp.redeem.RedeemModule.ADMIN_REDEEM_URL;
import static com.qxcmp.redeem.RedeemModuleNavigation.ADMIN_MENU_SETTINGS_REDEEM;

/**
 * @author Aaric
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class AdminRedeemSettingsPage extends AbstractQxcmpAdminFormPage<AdminRedeemSettingsForm> {
    public AdminRedeemSettingsPage(AdminRedeemSettingsForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_SETTINGS, ADMIN_MENU_SETTINGS_REDEEM);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("兑换码管理", ADMIN_REDEEM_URL, "兑换码设置");
    }
}
