package com.qxcmp.article.form;

import com.google.common.collect.Sets;
import com.qxcmp.user.User;
import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;

import java.util.Set;

/**
 * @author Aaric
 */
@Form(value = "管理栏目", submitText = "确认修改")
@Data
public class AdminNewsUserChannelAdminEditForm {

    @HiddenField
    private Long id;

    @AvatarField("栏目封面")
    private String cover;

    @TextInputField(value = "栏目名称", required = true, autoFocus = true)
    private String name;

    @TextAreaField(value = "栏目描述", rows = 4)
    private String description;

    @TextSelectionField(value = "管理员", itemValueIndex = "id", itemTextIndex = "username")
    private Set<User> admins = Sets.newHashSet();

    @HtmlField("栏目内容")
    private String content;
    private String contentQuill;
}
