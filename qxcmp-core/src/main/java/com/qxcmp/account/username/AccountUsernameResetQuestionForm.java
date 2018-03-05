package com.qxcmp.account.username;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.HiddenField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * 密保问题平台统一表单
 * <p>
 * 用户使用密码找回密码的时候需要填写的信息
 *
 * @author aaric
 * @see AccountUsernameResetForm
 */
@Form(submitText = "立即重置")
@Data
public class AccountUsernameResetQuestionForm {

    @HiddenField
    private String userId;

    @TextInputField(value = "问题一", readOnly = true)
    private String question1;

    @TextInputField(value = "答案", required = true, autoFocus = true)
    private String answer1;

    @TextInputField(value = "问题二", readOnly = true)
    private String question2;

    @TextInputField(value = "答案", required = true)
    private String answer2;

    @TextInputField(value = "问题三", readOnly = true)
    private String question3;

    @TextInputField(value = "答案", required = true)
    private String answer3;

}
