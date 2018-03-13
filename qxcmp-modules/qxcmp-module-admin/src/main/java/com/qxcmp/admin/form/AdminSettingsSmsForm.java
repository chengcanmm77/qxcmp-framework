package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form("短信服务配置")
@Data
public class AdminSettingsSmsForm {

    @TextInputField(value = "AccessKey", section = "基本配置", tooltip = "配置中的所有信息需要从阿里云短信服务控制台中获取")
    private String messageSmsAccessKey;

    @TextInputField(value = "AccessSecret", section = "基本配置")
    private String messageSmsAccessSecret;

    @TextInputField(value = "EndPoint", section = "基本配置")
    private String messageSmsEndPoint;

    @TextInputField(value = "主题名称", section = "基本配置")
    private String messageSmsTopicRef;

    @TextInputField(value = "短信签名ID", section = "基本配置")
    private String messageSmsSign;

    @TextInputField(value = "验证码模板", section = "业务配置", tooltip = "选择阿里云短信服务中的模板CODE")
    private String messageSmsCaptchaTemplateCode;
}
