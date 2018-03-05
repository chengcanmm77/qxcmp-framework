package com.qxcmp.web.view.elements.menu.item;

import com.qxcmp.core.navigation.AbstractNavigation;
import com.qxcmp.user.User;
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
public class BackendAccountMenuItem extends AbstractMenuItem {

    private User user;

    private List<AbstractNavigation> navigationList;

    public BackendAccountMenuItem(User user, List<AbstractNavigation> navigationList) {
        this.user = user;
        this.navigationList = navigationList;
    }

    @Override
    public String getFragmentName() {
        return "item-backend-account";
    }

    @Override
    public String getClassSuffix() {
        return "backend account link " + super.getClassSuffix();
    }
}
