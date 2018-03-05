package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.Form;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "编辑网站通知", action = SELF_ACTION)
public class AdminMessageSiteNotificationEditForm extends AdminMessageSiteNotificationNewForm {
}
