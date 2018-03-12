package com.qxcmp.redeem.form;

import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @author Aaric
 */
@Form(value = "生成兑换码", submitText = "立即生成")
@Data
public class AdminRedeemGenerateForm {

    @NotEmpty
    @TextSelectionField(value = "业务名称", required = true)
    private String type;

    @TextInputField(value = "业务数据", required = true, autoFocus = true)
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeField(value = "过期时间", enableDateRange = true, dateRangeLength = 30)
    private Date dateExpired;

    @Min(value = 1, message = "生成数量必须大于1")
    @Max(value = 100, message = "一次最多生成100张兑换券")
    @NumberField(value = "生成数量", tooltip = "一次最多生成100张兑换码", min = 1, max = 100)
    private int quantity = 1;
}
