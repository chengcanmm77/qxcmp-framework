package com.qxcmp.web.view.elements.divider;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

/**
 * 标准分隔符
 *
 * @author Aaric
 */
@Getter
@Setter
public class Divider extends AbstractComponent {

    /**
     * 是否为反转颜色
     */
    private boolean inverted;

    /**
     * 是否取消边距
     */
    private boolean fitted;

    /**
     * 是否为隐藏状态
     * <p>
     * 此属性将保留边距
     */
    private boolean hidden;

    /**
     * 是否增加边距
     */
    private boolean section;

    /**
     * 是否清除浮动
     */
    private boolean clearing;

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/divider";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        if (fitted) {
            stringBuilder.append(" fitted");
        }

        if (hidden) {
            stringBuilder.append(" hidden");
        }

        if (section) {
            stringBuilder.append(" section");
        }

        if (clearing) {
            stringBuilder.append(" clearing");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "divider";
    }

    public Divider setInverted() {
        this.inverted = true;
        return this;
    }

    public Divider setFitted() {
        this.fitted = true;
        return this;
    }

    public Divider setHidden() {
        this.hidden = true;
        return this;
    }

    public Divider setSection() {
        this.section = true;
        return this;
    }

    public Divider setClearing() {
        this.clearing = true;
        return this;
    }
}
