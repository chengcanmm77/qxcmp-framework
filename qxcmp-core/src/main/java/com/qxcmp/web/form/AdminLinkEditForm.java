package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.Form;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "编辑链接", action = SELF_ACTION)
public class AdminLinkEditForm extends AdminLinkNewForm {
}
