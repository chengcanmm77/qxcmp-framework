package com.qxcmp.web.view.elements.label;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 标签组
 *
 * @author Aaric
 */
@Getter
@Setter
public class Labels extends AbstractComponent {

    /**
     * 共享大小
     */
    private Size size = Size.NONE;

    /**
     * 共享颜色
     */
    private Color color = Color.NONE;

    private List<AbstractLabel> labels = Lists.newArrayList();

    public Labels addLabel(AbstractLabel label) {
        labels.add(label);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/label";
    }

    @Override
    public String getFragmentName() {
        return "labels";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return size.toString() + color.toString();
    }

    @Override
    public String getClassSuffix() {
        return "labels";
    }

    public Labels setSize(Size size) {
        this.size = size;
        return this;
    }

    public Labels setColor(Color color) {
        this.color = color;
        return this;
    }

}
