package com.qxcmp.web.view.elements.icon;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Flipped;
import com.qxcmp.web.view.support.Rotated;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 图标
 * <p>
 * 图标使用 Font Awesome 字体来渲染
 *
 * @author Aaric
 */
@Getter
@Setter
public class Icon extends AbstractComponent {

    /**
     * 图片元素名称
     */
    private String icon;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    /**
     * 是否为加载状态
     * <p>
     * 该状态会一直旋转图标元素
     */
    private boolean loading;

    /**
     * 该状态会移除左右两边的空白
     * <p>
     * 用于嵌套在文本中
     */
    private boolean fitted;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    /**
     * 是否渲染为超链接样式
     */
    private boolean link;

    /**
     * 是否反色
     */
    private boolean inverted;

    /**
     * 是否翻转
     */
    private Flipped flipped = Flipped.NONE;

    /**
     * 是否旋转
     */
    private Rotated rotated = Rotated.NONE;

    /**
     * 颜色
     */
    private Color color = Color.NONE;

    public Icon(String icon) {
        this.icon = icon;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/icon";
    }

    @Override
    public String getFragmentName() {
        return "icon";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (loading) {
            stringBuilder.append(" loading");
        }

        if (fitted) {
            stringBuilder.append(" fitted");
        }

        if (link) {
            stringBuilder.append(" link");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        stringBuilder.append(size.toString()).append(flipped.toString()).append(rotated.toString()).append(color.toString());

        stringBuilder.append(" ").append(icon);

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "icon";
    }

    public Icon setDisabled() {
        this.disabled = true;
        return this;
    }

    public Icon setLoading() {
        this.loading = true;
        return this;
    }

    public Icon setFitted() {
        this.fitted = true;
        return this;
    }

    public Icon setLink() {
        this.link = true;
        return this;
    }

    public Icon setInverted() {
        this.inverted = true;
        return this;
    }

    public Icon setSize(Size size) {
        this.size = size;
        return this;
    }

    public Icon setColor(Color color) {
        this.color = color;
        return this;
    }

    public Icon setFlipped(Flipped flipped) {
        this.flipped = flipped;
        return this;
    }

    public Icon setRotated(Rotated rotated) {
        this.rotated = rotated;
        return this;
    }

}
