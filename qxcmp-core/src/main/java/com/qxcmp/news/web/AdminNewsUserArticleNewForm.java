package com.qxcmp.news.web;

import com.qxcmp.news.Channel;
import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Form(value = "新建文章", submitText = "立即创建")
@Data
public class AdminNewsUserArticleNewForm {

    @AvatarField("文章封面")
    private String cover;

    @TextInputField(value = "文章标题", required = true, autoFocus = true)
    private String title;

    @TextInputField(value = "文章作者", required = true)
    private String author;

    @NotEmpty
    @TextSelectionField(value = "所属栏目", required = true, itemTextIndex = "name", itemValueIndex = "id")
    private List<Channel> channels;

    @TextAreaField(value = "文章摘要", rows = 4)
    private String digest;

    @HtmlField("文章内容")
    private String content;
    private String contentQuill;
}
