package com.qxcmp.shoppingmall.web;


import com.google.common.collect.Lists;
import com.qxcmp.core.validation.Phone;
import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import java.util.List;

@Form(value = "新建收货地址", submitText = "保存并使用")
@Data
public class MallConsigneeNewForm {

    @TextInputField(value = "收货人", required = true, autoFocus = true)
    private String consigneeName;

    @TextInputField(value = "详细地址", placeholder = "街道、楼牌号等", required = true)
    private String address;

    @PhoneField(value = "手机号", required = true)
    @Phone
    private String telephone;

    @EmailField("邮箱")
    @Email
    private String email;

    @DynamicField(value = "标签", itemHeaders = "标签名称")
    private List<String> labels = Lists.newArrayList();
}
