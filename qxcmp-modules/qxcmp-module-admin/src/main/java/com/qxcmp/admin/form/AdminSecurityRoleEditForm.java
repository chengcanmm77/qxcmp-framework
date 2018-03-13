package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.Form;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

/**
 * @author Aaric
 */
@Form(value = "编辑角色", action = SELF_ACTION)
public class AdminSecurityRoleEditForm extends AdminSecurityRoleNewForm {
}
