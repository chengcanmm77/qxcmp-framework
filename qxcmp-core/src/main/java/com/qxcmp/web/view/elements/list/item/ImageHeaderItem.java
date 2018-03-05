package com.qxcmp.web.view.elements.list.item;

import com.qxcmp.web.view.elements.html.Div;
import com.qxcmp.web.view.elements.image.AbstractImage;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片项目
 *
 * @author Aaric
 */
@Getter
@Setter
public class ImageHeaderItem extends AbstractListItem {

    /**
     * 图片
     */
    private AbstractImage image;

    /**
     * 标题
     */
    private Div header;

    /**
     * 描述
     */
    private Div description;

    public ImageHeaderItem(AbstractImage image, Div header) {
        this.image = image;
        this.header = header;
        this.header.setClassName("header");
    }

    public ImageHeaderItem(AbstractImage image, Div header, Div description) {
        this.image = image;
        this.header = header;
        this.description = description;
        this.header.setClassName("header");
        this.description.setClassName("description");
    }

    public ImageHeaderItem(AbstractImage image, String header) {
        this.image = image;
        this.header = new Div(header);
        this.header.setClassName("header");
    }

    public ImageHeaderItem(AbstractImage image, String header, String description) {
        this.image = image;
        this.header = new Div(header);
        this.description = new Div(description);
        this.header.setClassName("header");
        this.description.setClassName("description");
    }

    @Override
    public String getFragmentName() {
        return "item-image-header";
    }
}
