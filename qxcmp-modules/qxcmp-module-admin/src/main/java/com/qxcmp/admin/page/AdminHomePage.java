package com.qxcmp.admin.page;

import com.qxcmp.message.Feed;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.ContentHeader;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.modules.pagination.Pagination;
import com.qxcmp.web.view.support.Size;
import com.qxcmp.web.view.support.Wide;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 后台首页
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminHomePage extends AbstractQxcmpAdminPage {

    private final Page<Feed> feeds;

    @Override
    public void render() {
        addComponent(new VerticallyDividedGrid().setContainer().setCentered()
                .addItem(new Row()
                        .addCol(new Col().setMobileWide(Wide.SIXTEEN).setTabletWide(Wide.EIGHT).setComputerWide(Wide.EIGHT).addComponent(new Segment()
                                .addComponent(new ContentHeader("我的动态", Size.NONE).setDividing())
                                .addComponent(new com.qxcmp.web.view.views.Feed(feeds.getContent()))
                                .addComponent(new Pagination("", feeds.getNumber() + 1, (int) feeds.getTotalElements(), feeds.getSize()))
                        ))
                ));
    }
}
