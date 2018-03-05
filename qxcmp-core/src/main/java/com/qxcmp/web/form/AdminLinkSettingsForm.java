package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import lombok.Data;

import java.util.List;

@Form(value = "修改链接设置", submitText = "确认修改")
@Data
public class AdminLinkSettingsForm {

    @DynamicField(value = "链接类型", itemHeaders = "类型名称")
    private List<String> type;
}
