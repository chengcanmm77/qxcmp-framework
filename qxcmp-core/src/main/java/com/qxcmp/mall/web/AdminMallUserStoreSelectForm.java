package com.qxcmp.mall.web;

import com.qxcmp.mall.Store;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextSelectionField;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Form(submitText = "切换到该店铺")
@Data
public class AdminMallUserStoreSelectForm {

    @NotNull
    @TextSelectionField(value = "我的店铺", itemTextIndex = "name", itemValueIndex = "id")
    private Store store;

}
