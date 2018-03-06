package com.qxcmp.account.page;

import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.page.GenericQxcmpPage;
import com.qxcmp.web.view.support.Wide;

/**
 * 账户模块通用页面
 *
 * @author Aaric
 */
public abstract class BaseAccountPage extends GenericQxcmpPage {

    @Override
    public final void render() {
        Col col = new Col(Wide.SIXTEEN);
        renderContent(col);
        addComponent(new VerticallyDividedGrid().setVerticallyPadded().setTextContainer().addItem(new Row().addCol(col)));
    }

    /**
     * 渲染页面
     *
     * @param col 容器视图
     */
    public abstract void renderContent(Col col);
}
