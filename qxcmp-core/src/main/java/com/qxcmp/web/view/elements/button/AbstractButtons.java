package com.qxcmp.web.view.elements.button;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.ItemCount;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 按钮组抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public class AbstractButtons extends AbstractComponent {

    /**
     * 是否为基本按钮组
     */
    private boolean basic;

    /**
     * 是否为等宽按钮
     * <p>
     * 指定具体按钮个数
     */
    private ItemCount itemCount = ItemCount.NONE;

    /**
     * 是否为垂直按钮组
     */
    private boolean vertical;

    /**
     * 共享颜色
     */
    private Color color = Color.NONE;

    /**
     * 共享大小
     */
    private Size size = Size.NONE;

    /**
     * 子按钮
     */
    private List<AbstractButton> buttons = Lists.newArrayList();

    public AbstractButtons addButton(AbstractButton button) {
        buttons.add(button);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/button";
    }

    @Override
    public String getFragmentName() {
        return "buttons";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (basic) {
            stringBuilder.append(" basic");
        }

        if (vertical) {
            stringBuilder.append(" vertical");
        }

        stringBuilder.append(itemCount.toString()).append(color.toString()).append(size.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "buttons";
    }

    public AbstractButtons setBasic() {
        this.basic = true;
        return this;
    }

    public AbstractButtons setItemCount(ItemCount itemCount) {
        this.itemCount = itemCount;
        return this;
    }

    public AbstractButtons setVertical() {
        this.vertical = true;
        return this;
    }

    public AbstractButtons setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractButtons setSize(Size size) {
        this.size = size;
        return this;
    }
}
