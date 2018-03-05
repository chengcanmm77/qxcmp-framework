package com.qxcmp.web.form;

import com.qxcmp.core.validation.Port;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.NumberField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import javax.validation.constraints.Size;

@Form("邮件服务配置")
@Data
public class AdminMessageEmailSettingsForm {

    @TextInputField(value = "主机名", placeholder = "如smtp.sina.com", section = "基本配置")
    private String host;

    @Port
    @NumberField(value = "端口", min = 1, max = 65535, tooltip = "默认为25，端口号在1 - 65535之间", section = "基本配置")
    private String port;

    @TextInputField(value = "用户名", section = "基本配置")
    private String username;

    @TextInputField(value = "密码", section = "基本配置")
    private String password;

    @Size(max = 140)
    @TextInputField(value = "邮件主题", section = "账户激活邮件配置", maxLength = 140)
    private String activateSubject;

    @Size(max = 255)
    @TextInputField(value = "邮件内容", section = "账户激活邮件配置", tooltip = "使用${link}来代替激活链接", maxLength = 255)
    private String activateContent;

    @Size(max = 140)
    @TextInputField(value = "邮件主题", section = "密码重置邮件配置", maxLength = 140)
    private String resetSubject;

    @Size(max = 255)
    @TextInputField(value = "邮件内容", section = "密码重置邮件配置", tooltip = "使用${link}来代替重置链接", maxLength = 255)
    private String resetContent;

    @Size(max = 140)
    @TextInputField(value = "邮件主题", section = "账户绑定邮件配置", maxLength = 140)
    private String bindingSubject;

    @Size(max = 255)
    @TextInputField(value = "邮件内容", section = "账户绑定邮件配置", tooltip = "使用${link}来代替账户绑定链接", maxLength = 255)
    private String bindingContent;
}
