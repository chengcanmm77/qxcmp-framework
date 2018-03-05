package com.qxcmp.web.form;


import com.google.common.collect.Lists;
import com.qxcmp.security.Role;
import com.qxcmp.web.view.annotation.form.*;
import com.qxcmp.web.view.annotation.table.TableField;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author Aaric
 */
@Form(value = "发送站内信")
@Data
public class AdminMessageInnerMessageForm {

    @BooleanField("发送给所有用户")
    private boolean sendToAll;

    @TextSelectionField(value = "收件组", itemTextIndex = "name", itemValueIndex = "id")
    private Set<Role> group;

    @DynamicField(value = "收件人", itemHeaders = "收件人ID")
    private List<String> receivers = Lists.newArrayList();

    @TableField("标题")
    private String title;

    @HtmlField("内容")
    private String content;
    private String contentQuill;

}
