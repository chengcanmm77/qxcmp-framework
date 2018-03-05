package com.qxcmp.web.form;

import com.google.common.collect.Lists;
import com.qxcmp.config.SystemDictionaryItem;
import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import java.util.List;

@Form(value = "系统字典编辑")
@Data
public class AdminSettingsDictionaryForm {

    @TextInputField(value = "字典名称", readOnly = true)
    private String name;

    @DynamicField(value = "字典项目", itemFields = {"name", "priority"}, itemHeaders = {"名称", "顺序"}, maxCount = Integer.MAX_VALUE)
    private List<SystemDictionaryItem> items = Lists.newArrayList();
}
