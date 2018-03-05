package com.qxcmp.web.form;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import java.util.List;

@Form("百度链接提交")
@Data
public class AdminStatisticBaiduLinkForm {

    @TextInputField(value = "Site", section = "基本参数")
    private String site;

    @TextInputField(value = "Token", section = "基本参数")
    private String token;

    @TextInputField(value = "链接前缀", section = "连续链接提交", tooltip = "需要包含要提交的整个Url，比如http://www.example.com")
    private String linkPrefix;

    @TextInputField(value = "链接起始值", section = "连续链接提交")
    private int linkItemStart;

    @TextInputField(value = "链接结束值", section = "连续链接提交")
    private int linkItemEnd;

    @TextInputField(value = "链接后缀", section = "连续链接提交")
    private String linkSuffix;

    @DynamicField(value = "其他链接", itemHeaders = "链接", section = "其他链接提交")
    private List<String> links = Lists.newArrayList();
}
