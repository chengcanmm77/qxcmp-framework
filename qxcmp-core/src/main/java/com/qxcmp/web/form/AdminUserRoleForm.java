package com.qxcmp.web.form;

import com.google.common.collect.Sets;
import com.qxcmp.security.Role;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import com.qxcmp.web.view.annotation.form.TextSelectionField;
import lombok.Data;

import java.util.Set;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "编辑用户角色", action = SELF_ACTION)
@Data
public class AdminUserRoleForm {

    @TextInputField(value = "用户名", readOnly = true)
    private String username;

    @TextSelectionField(value = "拥有角色", tooltip = "修改角色以后，被修改的用户需要重新登录以后才能生效", itemTextIndex = "name", itemValueIndex = "id")
    private Set<Role> roles = Sets.newHashSet();
}
