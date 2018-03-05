package com.qxcmp.web.view.modules.dropdown.item;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.support.Direction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 子菜单项
 * <p>
 * 可嵌套
 *
 * @author Aaric
 */
@Getter
@Setter
public class MenuItem extends AbstractTextDropdownItem implements DropdownItem {

    /**
     * 子菜单打开方向，只支持 LEFT, RIGHT
     */
    private Direction direction = Direction.NONE;

    /**
     * 子选项
     */
    private List<AbstractDropdownItem> items = Lists.newArrayList();

    public MenuItem(String text) {
        super(text);
    }

    public MenuItem addItem(AbstractDropdownItem item) {
        items.add(item);
        return this;
    }

    public MenuItem addItems(List<? extends AbstractDropdownItem> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public String getFragmentName() {
        return "item-menu";
    }

    public MenuItem setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

}
