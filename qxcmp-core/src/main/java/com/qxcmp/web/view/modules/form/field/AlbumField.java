package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 照片墙字段
 * <p>
 * 用户可以上传图片并在列表中显示缩略图。当上传数量达到上限时，上传按钮消息
 *
 * @author Aaric
 */
@Getter
@Setter
public class AlbumField extends AbstractUploadField {

    /**
     * 支持的照片最大数量
     */
    private int maxCount;

    @Override
    public String getFragmentName() {
        return "field-album";
    }

    @Override
    public String getClassSuffix() {
        return "album " + super.getClassSuffix();
    }
}
