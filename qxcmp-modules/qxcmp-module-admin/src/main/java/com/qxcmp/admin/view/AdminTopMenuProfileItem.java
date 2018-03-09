package com.qxcmp.admin.view;

import com.qxcmp.core.navigation.AbstractNavigation;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.menu.item.AbstractMenuItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台顶部菜单专用菜单项
 *
 * @author Aaric
 */
@Getter
@Setter
public class AdminTopMenuProfileItem extends AbstractMenuItem {

    private User user;

    private List<AbstractNavigation> navigationList;

    public AdminTopMenuProfileItem(User user, List<AbstractNavigation> navigationList) {
        this.user = user;
        this.navigationList = navigationList;
    }

    @Override
    public String getFragmentName() {
        return "item-profile";
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/admin/menu";
    }

    @Override
    public String getClassSuffix() {
        return "backend account link " + super.getClassSuffix();
    }
}
