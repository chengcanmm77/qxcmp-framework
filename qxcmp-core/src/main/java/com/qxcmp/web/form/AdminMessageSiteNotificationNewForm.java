package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Aaric
 */
@Form("新建网站通知")
@Data
public class AdminMessageSiteNotificationNewForm {

    @TextSelectionField(value = "通知类型")
    private String type;

    @DateTimeField(value = "生效时间", disableMinute = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateStart;

    @DateTimeField(value = "结束时间", disableMinute = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateEnd;

    @TextInputField(value = "通知标题", required = true, autoFocus = true)
    private String title;

    @TextAreaField(value = "通知内容", maxLength = 255)
    private String content;
}
