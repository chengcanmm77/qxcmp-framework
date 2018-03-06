package com.qxcmp.account.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.PasswordField;
import com.qxcmp.web.view.modules.form.FormMethod;
import lombok.Data;

import javax.validation.constraints.Size;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

/**
 * 账户密码重置平台统一表单
 * <p>
 * 该表单用户重置账户密码，适用于所有重置类型
 * <p>
 * 用户在重置链接中获取到该表单，自动关联重置的账户信息
 *
 * @author aaric
 */
@Form(submitText = "重置密码", method = FormMethod.POST, action = SELF_ACTION)
@Data
public class AccountResetForm {

    /**
     * 新的账户密码
     * <p>
     * 用户登录时候需要输入的密码
     */
    @Size(min = 6, max = 20, message = "{Size.password}")
    @PasswordField(value = "新的密码", placeholder = "请使用至少两种以上的字符组合", maxLength = 20)
    private String password;

    /**
     * 确认密码
     * <p>
     * 保证用户两次输入的密码一致
     */
    @Size(min = 6, max = 20, message = "{Size.password}")
    @PasswordField(value = "确认密码", placeholder = "请再次输入您的登录密码", maxLength = 20)
    private String passwordConfirm;
}
