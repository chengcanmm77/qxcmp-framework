package com.qxcmp.news.web;

import com.qxcmp.web.view.annotation.form.Form;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "编辑文章", submitText = "确认修改", action = SELF_ACTION)
public class AdminNewsUserArticleEditForm extends AdminNewsUserArticleNewForm {
}
