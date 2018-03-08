package com.qxcmp.advertisement.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.advertisement.form.AdminAdvertisementNewForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.advertisement.AdvertisementModule.ADMIN_ADVERTISEMENT_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminAdvertisementNewPage extends AbstractQxcmpAdminFormPage<AdminAdvertisementNewForm> {

    public AdminAdvertisementNewPage(AdminAdvertisementNewForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("广告管理", ADMIN_ADVERTISEMENT_URL, "新建广告");
    }
}
