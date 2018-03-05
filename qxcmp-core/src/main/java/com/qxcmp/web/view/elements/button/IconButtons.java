package com.qxcmp.web.view.elements.button;

import lombok.Getter;
import lombok.Setter;

/**
 * 图标按钮组
 *
 * @author Aaric
 */
@Getter
@Setter
public class IconButtons extends Buttons {

    public AbstractButtons addButton(IconButton button) {
        return super.addButton(button);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " icon";
    }
}
