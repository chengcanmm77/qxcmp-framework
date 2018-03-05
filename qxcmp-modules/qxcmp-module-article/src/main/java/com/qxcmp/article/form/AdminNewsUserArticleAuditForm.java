package com.qxcmp.article.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(submitText = "确认申请审核", action = SELF_ACTION)
@Data
public class AdminNewsUserArticleAuditForm {

    @NotEmpty
    @TextInputField(value = "申请说明", required = true, autoFocus = true)
    private String auditRequest;
}
