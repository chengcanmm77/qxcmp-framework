package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "编辑用户角色", action = SELF_ACTION)
@Data
public class AdminUserStatusForm {

    @TextInputField(value = "用户名", readOnly = true)
    private String username;

    @BooleanField(value = "是否禁用")
    private boolean disabled;

    @BooleanField(value = "是否锁定")
    private boolean locked;

    @BooleanField(value = "是否过期")
    private boolean expired;

    @BooleanField(value = "密码过期")
    private boolean credentialExpired;
}
