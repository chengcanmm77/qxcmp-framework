package com.qxcmp.web.view.modules.dropdown.item;

import com.qxcmp.web.view.elements.image.AbstractImage;
import com.qxcmp.web.view.elements.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageItem extends AbstractTextDropdownItem implements DropdownItem, SelectionItem {

    /**
     * 图片
     */
    private AbstractImage image;

    public ImageItem(String text, String source) {
        this(text, new Image(source));
    }

    public ImageItem(String text, AbstractImage image) {
        super(text);
        this.image = image;
    }

    public ImageItem(String text, String value, String source) {
        super(text, value);
        this.image = new Image(source);
    }


    public ImageItem(String text, String value, AbstractImage image) {
        super(text, value);
        this.image = image;
    }

    @Override
    public String getFragmentName() {
        return "item-image";
    }
}
