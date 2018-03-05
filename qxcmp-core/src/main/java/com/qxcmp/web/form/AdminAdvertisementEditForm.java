package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;

import static com.qxcmp.web.view.support.utils.FormHelper.SELF_ACTION;

@Form(value = "编辑广告", action = SELF_ACTION)
@Data
public class AdminAdvertisementEditForm {

    @AvatarField("广告图片")
    private String image;

    @TextSelectionField("广告类型")
    private String type;

    @TextInputField("广告名称")
    private String title;

    @TextInputField("广告链接")
    private String link;

    @NumberField("顺序")
    private int adOrder;

    @BooleanField("新窗口打开")
    private boolean black;
}
