package com.qxcmp.mall.web;

import com.qxcmp.user.User;
import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Form(value = "创建店铺", submitText = "立即创建")
@Data
public class AdminMallStoreNewForm {

    @AvatarField("封面")
    private String cover;

    @TextInputField(value = "店铺名称", required = true, autoFocus = true, tooltip = "名称必须唯一")
    private String name;

    @NotNull
    @TextSelectionField(value = "店铺所有者", required = true, itemValueIndex = "id", itemTextIndex = "username")
    private User owner;

    @BooleanField(value = "外部店铺", tooltip = "外部店铺名称将会显示在商品页面")
    private boolean external;
}
