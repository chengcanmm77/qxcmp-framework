package com.qxcmp.web.view.elements.input;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.button.AbstractButton;
import com.qxcmp.web.view.modules.dropdown.Dropdown;
import lombok.Getter;
import lombok.Setter;

/**
 * 带按钮的输入框
 *
 * @author Aaric
 */
@Getter
@Setter
public class ActionInput extends AbstractComponent {

    /**
     * 输入框
     */
    private AbstractInput input;

    /**
     * 按钮
     */
    private AbstractButton button;

    /**
     * 如果不是按钮，还可以设置为下拉框
     */
    private Dropdown dropdown;

    /**
     * 按钮是否在左边显示
     */
    private boolean leftAction;

    public ActionInput(AbstractInput input, AbstractButton button) {
        this.input = input;
        this.button = button;
    }

    public ActionInput(AbstractInput input, Dropdown dropdown) {
        this.input = input;
        this.dropdown = dropdown;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/input";
    }

    @Override
    public String getFragmentName() {
        return "action";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return input.getClassContent() + (leftAction ? " left" : "");
    }

    @Override
    public String getClassSuffix() {
        return "action input";
    }

    public ActionInput setLeftAction() {
        setLeftAction(true);
        return this;
    }
}
