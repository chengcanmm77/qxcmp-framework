package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 上传类字段抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public class AbstractUploadField extends AbstractFormField {

    /**
     * 最大图片大小(KB)
     */
    private long maxSize;
}
