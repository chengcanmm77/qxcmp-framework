package com.qxcmp.mall.web;

import com.qxcmp.web.view.annotation.form.Form;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "编辑店铺", submitText = "确认修改", action = SELF_ACTION)
public class AdminMallStoreEditForm extends AdminMallStoreNewForm {
}
