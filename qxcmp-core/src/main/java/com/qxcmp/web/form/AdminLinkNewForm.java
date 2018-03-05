package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.NumberField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import com.qxcmp.web.view.annotation.form.TextSelectionField;
import lombok.Data;

@Form(value = "添加链接", submitText = "确认添加")
@Data
public class AdminLinkNewForm {

    @TextInputField(value = "标题", required = true, autoFocus = true)
    private String title;

    @TextSelectionField("类型")
    private String type;

    @TextInputField(value = "超链接", required = true)
    private String href;

    @TextSelectionField("打开方式")
    private String target;

    @NumberField(value = "优先级", tooltip = "越小越优先")
    private int sort;
}
