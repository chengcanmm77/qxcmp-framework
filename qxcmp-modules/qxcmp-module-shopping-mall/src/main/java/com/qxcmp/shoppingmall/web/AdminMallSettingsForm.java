package com.qxcmp.shoppingmall.web;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import lombok.Data;

import java.util.List;

@Form("商城设置")
@Data
public class AdminMallSettingsForm {

    @DynamicField(value = "商品类别", itemHeaders = "类别名称", tooltip = "所有商品只能在此类别里面进行选择")
    private List<String> catalogs = Lists.newArrayList();
}
