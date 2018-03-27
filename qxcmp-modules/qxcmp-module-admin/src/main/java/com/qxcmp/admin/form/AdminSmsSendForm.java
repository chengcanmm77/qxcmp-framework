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

    @TextAreaField(value = "短信参数", placeholder = "JSON格式参数")
    private String parameter;
}
