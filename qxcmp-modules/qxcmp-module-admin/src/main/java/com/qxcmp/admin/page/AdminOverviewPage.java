package com.qxcmp.admin.page;

import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 后台概览页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminOverviewPage extends AbstractQxcmpAdminPage {

    private final Overview overview;

    @Override
    public void render() {
        addComponent(new TextContainer().addComponent(overview));
    }
}
