package com.qxcmp.admin.view;

import com.qxcmp.web.view.elements.menu.VerticalMenu;
import com.qxcmp.web.view.elements.menu.item.AbstractMenuItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 后台移动端页面导航
 *
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class AdminTopMenuMobileItem extends AbstractMenuItem {

    private final VerticalMenu verticalMenu;

    @Override
    public String getFragmentName() {
        return "item-mobile";
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/admin/menu";
    }

    @Override
    public String getClassSuffix() {
        return "backend mobile link icon " + super.getClassSuffix();
    }
}
