package com.qxcmp.web.view.modules.form.field;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 邮箱输入框
 * <p>
 * 用于需要输入邮箱的情况
 * <p>
 * 当邮箱格式不正确的时候会自动标记为错误状态
 * <p>
 * 并且会自动弹出常用邮箱结尾的下拉框
 *
 * @author Aaric
 */
@Getter
@Setter
public class EmailField extends TextInputField {

    /**
     * 自动填充邮箱后缀列表
     */
    private List<String> suffixList = ImmutableList.of(
            "qq.com",
            "163.com",
            "sina.com",
            "gmail.com",
            "hotmail.com",
            "msn.com",
            "foxmail.com",
            "yahoo.com",
            "21cn.com"
    );

    @Override
    public String getFragmentName() {
        return "field-email";
    }

    @Override
    public String getClassSuffix() {
        return "email " + super.getClassSuffix();
    }
}
