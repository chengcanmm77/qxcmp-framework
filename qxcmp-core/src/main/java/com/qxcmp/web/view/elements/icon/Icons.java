package com.qxcmp.web.view.elements.icon;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 图标组
 * <p>
 * 图标组为两个图标叠加在一起
 *
 * @author Aaric
 */
@Getter
@Setter
public class Icons extends AbstractComponent {

    /**
     * 共享大小
     */
    private Size size = Size.NONE;

    /**
     * 第一个图标
     */
    private Icon icon1;

    /**
     * 第二个图标
     */
    private Icon icon2;

    public Icons(Icon icon1, Icon icon2) {
        this.icon1 = icon1;
        this.icon2 = icon2;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/icon";
    }

    @Override
    public String getFragmentName() {
        return "icons";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(size.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "icons";
    }

    public Icons setSize(Size size) {
        this.size = size;
        return this;
    }
}
