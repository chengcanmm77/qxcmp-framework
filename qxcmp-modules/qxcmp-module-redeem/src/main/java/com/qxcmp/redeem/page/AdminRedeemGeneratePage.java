package com.qxcmp.redeem.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.redeem.form.AdminRedeemGenerateForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.redeem.RedeemModule.ADMIN_REDEEM_URL;

/**
 * @author Aaric
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class AdminRedeemGeneratePage extends AbstractQxcmpAdminFormPage<AdminRedeemGenerateForm> {
    public AdminRedeemGeneratePage(AdminRedeemGenerateForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("兑换码管理", ADMIN_REDEEM_URL, "生成兑换码");
    }
}
