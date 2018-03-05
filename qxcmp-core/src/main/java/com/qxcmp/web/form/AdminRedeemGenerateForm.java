package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Form(value = "生成兑换码", submitText = "立即生成")
@Data
public class AdminRedeemGenerateForm {

    @NotEmpty
    @TextSelectionField(value = "业务名称", required = true)
    private String type;

    @TextInputField(value = "业务数据", required = true, autoFocus = true)
    private String content;

    @HiddenField
    private Date dateExpired;

    @Min(value = 1, message = "生成数量必须大于1")
    @Max(value = 100, message = "一次最多生成100张兑换券")
    @NumberField(value = "生成数量", tooltip = "一次最多生成100张兑换码", max = 100)
    private int quantity;
}
