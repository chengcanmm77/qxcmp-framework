package com.qxcmp.web.view.page;

import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.views.Overview;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 平台默认概览页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class DefaultOverviewPage extends AbstractOverviewPage {

    public DefaultOverviewPage(Overview overview) {
        super(overview);
    }

    @Override
    public void render() {
        addComponent(new VerticallyDividedGrid().setVerticallyPadded().setTextContainer().addItem(new Row().addCol(new Col(Wide.SIXTEEN)
                .addComponent(overview))));
    }
}
