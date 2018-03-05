package com.qxcmp.web.view.elements.container;

/**
 * 文本容器
 * <p>
 * 文本容器会比标准容器具有更窄的宽度
 *
 * @author aaric
 */
public class TextContainer extends AbstractContainer {

    @Override
    public String getClassContent() {
        return super.getClassContent() + " text";
    }
}
