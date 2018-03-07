package com.qxcmp.account.page;

import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.page.AbstractQxcmpPage;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Wide;
import org.apache.commons.lang3.StringUtils;

/**
 * 账户模块通用页面
 *
 * @author Aaric
 */
public abstract class BaseAccountPage extends AbstractQxcmpPage {

    /**
     * 通用页面头部组件
     *
     * @param subTitle 子标题
     *
     * @return 头部组件
     */
    protected PageHeader getPageHeader(String subTitle) {
        PageHeader pageHeader = new PageHeader(HeaderType.H2, siteService.getTitle());
        if (StringUtils.isNotBlank(subTitle)) {
            pageHeader.setSubTitle(subTitle);
        }
        pageHeader.setImage(new Image(siteService.getLogo(), "/login"));
        pageHeader.setDividing();
        pageHeader.setAlignment(Alignment.LEFT);
        return pageHeader;
    }

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
