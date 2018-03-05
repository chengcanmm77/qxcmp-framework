package com.qxcmp.web.view.elements.image;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 图片组
 *
 * @author Aaric
 */
@Getter
@Setter
public class Images extends AbstractComponent {

    /**
     * 共享图片大小
     */
    private Size size = Size.NONE;

    private List<Image> images = Lists.newArrayList();

    public Images addImage(Image image) {
        images.add(image);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/image";
    }

    @Override
    public String getFragmentName() {
        return "images";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return size.toString();
    }

    @Override
    public String getClassSuffix() {
        return "images";
    }

    public Images setSize(Size size) {
        this.size = size;
        return this;
    }
}
