package com.qxcmp.web.view.elements.statistic;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Floated;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class Statistic extends AbstractComponent {

    private String label;
    private String value;

    private boolean horizontal;
    private boolean inverted;
    private Color color = Color.NONE;
    private Size size = Size.NONE;
    private Floated floated = Floated.NONE;

    /**
     * 默认值在上面，标签在下面，开启以后值在下面，标签在上面
     */
    private boolean flipLabel;

    public Statistic(String label, String value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/statistic";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        StringBuilder stringBuilder = new StringBuilder();

        if (horizontal) {
            stringBuilder.append(" horizontal");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        return stringBuilder.append(color.toString()).append(size.toString()).append(floated.toString()).toString();
    }

    @Override
    public String getClassSuffix() {
        return "statistic";
    }

    public Statistic setHorizontal() {
        this.horizontal = true;
        return this;
    }

    public Statistic setInverted() {
        this.inverted = true;
        return this;
    }

    public Statistic setColor(Color color) {
        this.color = color;
        return this;
    }

    public Statistic setSize(Size size) {
        this.size = size;
        return this;
    }

    public Statistic setFloated(Floated floated) {
        this.floated = floated;
        return this;
    }

    public Statistic setFlipLabel() {
        this.flipLabel = true;
        return this;
    }
}
