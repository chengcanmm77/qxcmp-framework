package com.qxcmp.web.view.elements.button;

/**
 * 带图标标签的按钮组
 *
 * @author Aaric
 */
public class LabeledIconButtons extends Buttons {
    public AbstractButtons addButton(LabeledIconButton button) {
        return super.addButton(button);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " labeled icon";
    }
}
