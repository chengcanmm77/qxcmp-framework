package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextAreaField;
import lombok.Data;

@Form("公众号菜单")
@Data
public class AdminWeixinMenuForm {

    @TextAreaField(value = "菜单内容", autoFocus = true, rows = 50, maxLength = 5000)
    private String content;
}
