package com.qxcmp.web.form;

import com.google.common.collect.Lists;
import com.qxcmp.message.SmsTemplate;
import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import java.util.List;

@Form("短信服务配置")
@Data
public class AdminMessageSmsSettingsForm {

    @TextInputField(value = "AccessKey", section = "基本配置", tooltip = "配置中的所有信息需要从阿里云短信服务控制台中获取")
    private String accessKey;

    @TextInputField(value = "AccessSecret", section = "基本配置")
    private String accessSecret;

    @TextInputField(value = "EndPoint", section = "基本配置")
    private String endPoint;

    @TextInputField(value = "主题名称", section = "基本配置")
    private String topicRef;

    @TextInputField(value = "短信签名ID", section = "基本配置")
    private String sign;

    @TextInputField(value = "验证码模板", section = "业务配置", tooltip = "选择阿里云短信服务中的模板CODE")
    private String captchaTemplate;

    @DynamicField(value = "其他模板", section = "业务配置", itemHeaders = {"模板主键", "业务名称", "模板Code"}, itemFields = {"id", "title", "template"})
    private List<SmsTemplate> templates = Lists.newArrayList();
}
