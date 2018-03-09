package com.qxcmp.link.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.link.form.AdminLinkNewForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.link.LinkModule.ADMIN_LINK_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminLinkNewPage extends AbstractQxcmpAdminFormPage<AdminLinkNewForm> {

    public AdminLinkNewPage(AdminLinkNewForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("链接管理", ADMIN_LINK_URL, "新建链接");
    }
}
