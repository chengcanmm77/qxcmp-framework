package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "添加子地区", submitText = "确认添加", action = SELF_ACTION)
@Data
public class AdminSettingsRegionNewForm {

    @NotEmpty
    @TextInputField(value = "上级城市", readOnly = true)
    private String parent;


    @NotEmpty
    @TextInputField(value = "地区代码", required = true, autoFocus = true, tooltip = "被添加的地区只能为区、县，添加以后不能删除，只能禁用")
    private String code;

    @NotEmpty
    @TextInputField(value = "地区名称", required = true)
    private String name;
}
