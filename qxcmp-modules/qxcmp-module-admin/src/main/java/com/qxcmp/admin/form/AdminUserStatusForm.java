package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

/**
 * @author Aaric
 */
@Form(value = "编辑用户角色", action = SELF_ACTION)
@Data
public class AdminUserStatusForm {

    @TextInputField(value = "用户名", readOnly = true)
    private String username;

    @BooleanField(value = "是否启用")
    private Boolean enabled;

    @BooleanField(value = "账户未锁定")
    private Boolean accountNonLocked;

    @BooleanField(value = "账户未过期")
    private Boolean accountNonExpired;

    @BooleanField(value = "密码未过期")
    private Boolean credentialsNonExpired;
}
