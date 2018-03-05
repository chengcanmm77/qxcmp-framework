package com.qxcmp.web.view.elements.button;

import com.qxcmp.web.view.elements.label.Label;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * 带标签的按钮
 *
 * @author Aaric
 */
@Getter
@Setter
public class LabeledButton extends AbstractButton {

    /**
     * 标签是否靠左
     */
    private boolean leftLabel;

    /**
     * 按钮类型
     */
    private AbstractButton button;

    /**
     * 标签类型
     */
    private Label label;

    public LabeledButton(String buttonText, String labelText) {
        this.button = new Button(buttonText);
        this.label = new Label(labelText);
    }

    public LabeledButton(AbstractButton button, Label label) {
        this.button = button;
        this.label = label;
    }

    public LabeledButton(AbstractButton button, Label label, String url) {
        super(url);
        this.button = button;
        this.label = label;
    }

    public LabeledButton(AbstractButton button, Label label, String url, AnchorTarget anchorTarget) {
        super(url, anchorTarget);
        this.button = button;
        this.label = label;
    }

    @Override
    public String getFragmentName() {
        return "label";
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + (leftLabel ? " left labeled" : " labeled");
    }

    @Override
    public AbstractButton setColor(Color color) {
        this.button.setColor(color);
        this.label.setColor(color);
        return super.setColor(color);
    }

    public LabeledButton setLeftLabel() {
        this.leftLabel = true;
        return this;
    }
}
