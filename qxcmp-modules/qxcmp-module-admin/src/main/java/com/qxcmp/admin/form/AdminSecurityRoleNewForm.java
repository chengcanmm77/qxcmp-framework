package com.qxcmp.admin.form;

import com.google.common.collect.Sets;
import com.qxcmp.security.Privilege;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import com.qxcmp.web.view.annotation.form.TextSelectionField;
import lombok.Data;

import java.util.Set;

/**
 * @author Aaric
 */
@Form("新建角色")
@Data
public class AdminSecurityRoleNewForm {

    @TextInputField(value = "角色名称", required = true, autoFocus = true)
    private String name;

    @TextInputField("角色描述")
    private String description;

    @TextSelectionField(value = "角色权限", itemTextIndex = "name", itemValueIndex = "id")
    private Set<Privilege> privileges = Sets.newHashSet();
}
