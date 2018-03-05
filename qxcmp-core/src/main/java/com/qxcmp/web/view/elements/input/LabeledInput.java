package com.qxcmp.web.view.elements.input;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.label.AbstractLabel;
import lombok.Getter;
import lombok.Setter;

/**
 * 带标签的输入框
 *
 * @author Aaric
 */
@Getter
@Setter
public class LabeledInput extends AbstractComponent {

    /**
     * 输入框
     */
    private AbstractInput input;

    /**
     * 标签
     */
    private AbstractLabel label;

    /**
     * 额外标签
     */
    private AbstractLabel extraLabel;

    /**
     * 标签是否靠右显示
     */
    private boolean rightLabel;

    public LabeledInput(AbstractInput input, AbstractLabel label) {
        this.input = input;
        this.label = label;
    }

    public LabeledInput(AbstractInput input, AbstractLabel label, AbstractLabel extraLabel) {
        this.input = input;
        this.label = label;
        this.extraLabel = extraLabel;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/input";
    }

    @Override
    public String getFragmentName() {
        return "label";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return input.getClassContent() + (rightLabel ? " right" : "");
    }

    @Override
    public String getClassSuffix() {
        return "labeled input";
    }

    public LabeledInput setRightLabel() {
        setRightLabel(true);
        return this;
    }
}
