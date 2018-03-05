package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 头像上传表单
 *
 * @author Aaric
 */
@Getter
@Setter
public class AvatarField extends AbstractUploadField {


    @Override
    public String getFragmentName() {
        return "field-avatar";
    }

    @Override
    public String getClassSuffix() {
        return "avatar " + super.getClassSuffix();
    }
}
