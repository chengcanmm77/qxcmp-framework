package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import com.qxcmp.web.view.annotation.form.TextSelectionField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form(value = "密保问题", submitText = "设置密保")
@Data
public class AdminProfileSecurityQuestionForm {

    @TextSelectionField("密保问题一")
    private String question1;

    @TextInputField("答案")
    private String answer1;

    @TextSelectionField("密保问题二")
    private String question2;

    @TextInputField("答案")
    private String answer2;

    @TextSelectionField("密保问题三")
    private String question3;

    @TextInputField("答案")
    private String answer3;

}
