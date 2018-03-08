package com.qxcmp.admin.page;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 后台通用表单页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class GenericAdminFormPage extends AbstractQxcmpAdminFormPage {

    private final List<String> breadcrumb;

    public GenericAdminFormPage(Object form, BindingResult bindingResult, List<String> breadcrumb) {
        super(form, bindingResult);
        this.breadcrumb = breadcrumb;
    }

    @Override
    protected void preRender() {

    }

    @Override
    protected void postRender() {

    }

    @Override
    protected List<String> getBreadcrumb() {
        return breadcrumb;
    }
}
