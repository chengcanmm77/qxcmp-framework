package com.qxcmp.web.view.support.utils;

import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.message.ErrorMessage;
import com.qxcmp.web.view.modules.form.AbstractForm;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * 视图生成工具
 *
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class ViewHelper {

    private final FormHelper formHelper;

    private final TableHelper tableHelper;

    private final OverviewHelper overviewHelper;

    public AbstractForm nextForm(Object form) {
        return formHelper.convert(form);
    }

    public ErrorMessage nextFormErrorMessage(BindingResult bindingResult, Object form) {
        return formHelper.convertToErrorMessage(bindingResult, form);
    }

    public Overview nextOverview(Icon icon, String title, String subTitle) {
        return overviewHelper.nextOverview(icon, title, subTitle);
    }

    public Overview nextOverview(String icon, String title, String subTitle) {
        return overviewHelper.nextOverview(new Icon(icon), title, subTitle);
    }

    public Overview nextInfoOverview(String title) {
        return nextInfoOverview(title, "");
    }

    public Overview nextInfoOverview(String title, String subTitle) {
        return nextOverview(new Icon("info circle"), title, subTitle);
    }

    public Overview nextSuccessOverview(String title) {
        return nextSuccessOverview(title, "");
    }

    public Overview nextSuccessOverview(String title, String subTitle) {
        return nextOverview(new Icon("check circle").setColor(Color.GREEN), title, subTitle);
    }

    public Overview nextWarningOverview(String title) {
        return nextWarningOverview(title, "");
    }

    public Overview nextWarningOverview(String title, String subTitle) {
        return nextOverview(new Icon("warning circle").setColor(Color.ORANGE), title, subTitle);
    }
}
