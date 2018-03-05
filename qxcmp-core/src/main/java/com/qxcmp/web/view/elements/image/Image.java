package com.qxcmp.web.view.elements.image;

import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Floated;
import com.qxcmp.web.view.support.Size;
import com.qxcmp.web.view.support.VerticalAlignment;
import lombok.Getter;
import lombok.Setter;

/**
 * 一般图片
 *
 * @author aaric
 */
@Getter
@Setter
public class Image extends AbstractImage {

    /**
     * 样式：是否显示边框
     */
    private boolean bordered;

    /**
     * 样式：是否占满父容器
     */
    private boolean fluid;

    /**
     * 是否为圆角
     */
    private boolean rounded;

    /**
     * 是否居中显示
     */
    private boolean centered;

    /**
     * 是否为圆形
     */
    private boolean circular;

    /**
     * 垂直对齐方式
     */
    private VerticalAlignment verticalAlignment = VerticalAlignment.NONE;

    /**
     * 浮动类型
     */
    private Floated floated = Floated.NONE;

    /**
     * 图片大小
     */
    private Size size = Size.NONE;

    public Image(String image) {
        super(image);
    }

    public Image(String image, String url) {
        super(image, url);
    }

    public Image(String image, String url, AnchorTarget target) {
        super(image, url, target);
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (bordered) {
            stringBuilder.append(" bordered");
        }

        if (fluid) {
            stringBuilder.append(" fluid");
        }

        if (rounded) {
            stringBuilder.append(" rounded");
        }

        if (centered) {
            stringBuilder.append(" centered");
        }

        if (circular) {
            stringBuilder.append(" circular");
        }

        stringBuilder.append(verticalAlignment.toString()).append(floated.toString()).append(size.toString());

        return stringBuilder.toString();
    }

    public Image setBordered() {
        this.bordered = true;
        return this;
    }

    public Image setFluid() {
        this.fluid = true;
        return this;
    }

    public Image setRounded() {
        this.rounded = true;
        return this;
    }

    public Image setCentered() {
        this.centered = true;
        return this;
    }

    public Image setCircular() {
        this.circular = true;
        return this;
    }

    public Image setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    public Image setFloated(Floated floated) {
        this.floated = floated;
        return this;
    }

    public Image setSize(Size size) {
        this.size = size;
        return this;
    }
}
