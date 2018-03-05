package com.qxcmp.web.view.elements.list.item;

import com.qxcmp.web.view.elements.image.AbstractImage;
import com.qxcmp.web.view.elements.image.Avatar;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片项目
 *
 * @author Aaric
 */
@Getter
@Setter
public class ImageTextItem extends AbstractListItem {

    /**
     * 图片
     */
    private AbstractImage image;

    /**
     * 文本
     */
    private String text;

    public ImageTextItem(AbstractImage image, String text) {
        this.image = image;
        this.text = text;
    }

    public ImageTextItem(String source, String text) {
        this.image = new Avatar(source);
        this.text = text;
    }

    @Override
    public String getFragmentName() {
        return "item-image-text";
    }
}
