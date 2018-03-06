package com.qxcmp.account.page;

import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.segment.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 密码找回页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class ResetPage extends BaseAccountPage {

    private final Object form;
    private final BindingResult bindingResult;

    @Override
    public void renderContent(Col col) {
        col.addComponent(new Segment()
                .addComponent(getPageHeader("找回密码"))
                .addComponent(viewHelper.nextForm(form, bindingResult)));
    }
}
