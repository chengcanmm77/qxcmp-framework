package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadField extends AbstractUploadField {

    private int maxCount;

    private String text;

    @Override
    public String getFragmentName() {
        return "field-file-upload";
    }

    @Override
    public String getClassContent() {
        return "qxcmp-file-upload" + super.getClassContent();
    }
}
