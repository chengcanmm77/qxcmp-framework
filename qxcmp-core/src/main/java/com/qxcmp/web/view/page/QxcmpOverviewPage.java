package com.qxcmp.web.view.page;

import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 平台概览页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class QxcmpOverviewPage extends AbstractQxcmpPage {

    private final Overview overview;

    @Override
    public void render() {
        addComponent(new VerticallyDividedGrid().setVerticallyPadded().setTextContainer().addItem(new Row().addCol(new Col(Wide.SIXTEEN)
                .addComponent(overview))));
    }
}
