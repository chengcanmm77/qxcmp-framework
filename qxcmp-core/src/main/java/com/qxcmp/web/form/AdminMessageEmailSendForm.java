package com.qxcmp.web.form;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextAreaField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import java.util.List;

/**
 * 短信发送测试表单
 *
 * @author aaric
 */
@Form(value = "邮件发送服务", submitText = "发送验证短信")
@Data
public class AdminMessageEmailSendForm {

    @TextInputField("邮件主题")
    private String subject;

    @DynamicField(value = "收件人", itemHeaders = "邮箱", tooltip = "最多支持同时发送20个邮箱", maxCount = 20)
    private List<String> to = Lists.newArrayList();

    @DynamicField(value = "抄送", itemHeaders = "邮箱", tooltip = "最多支持同时发送20个邮箱", maxCount = 20)
    private List<String> cc = Lists.newArrayList();

    @TextAreaField(value = "正文", rows = 30)
    private String content;
}
