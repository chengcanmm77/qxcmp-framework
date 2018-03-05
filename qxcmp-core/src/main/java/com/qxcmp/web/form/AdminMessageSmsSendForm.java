package com.qxcmp.web.form;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import java.util.List;

/**
 * 短信发送测试表单
 *
 * @author aaric
 */
@Form(value = "短信发送服务", submitText = "发送验证短信")
@Data
public class AdminMessageSmsSendForm {

    @TextInputField("短信模板Code")
    private String templateCode;

    @DynamicField(value = "短信参数", itemHeaders = {"变量名", "变量值"}, itemFields = {"key", "value"})
    private List<SmsMessageParameter> parameters = Lists.newArrayList();

    @DynamicField(value = "发送目标", itemHeaders = "手机号", tooltip = "最多支持同时发送20个手机号", maxCount = 20)
    private List<String> phones = Lists.newArrayList();
}
