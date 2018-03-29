package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextAreaField;
import com.qxcmp.web.view.annotation.form.TextSelectionField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form("发送短信")
@Data
public class AdminSmsSendForm {

    @TextSelectionField(value = "业务名称")
    private String name;

    @TextAreaField(value = "手机号", placeholder = "多个手机号用换行分割")
    private String phones;

    @TextAreaField(value = "短信参数", placeholder = "格式：每行一个参数，[参数：参数值]")
    private String parameter;
}
