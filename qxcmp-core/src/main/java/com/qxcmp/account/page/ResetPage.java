package com.qxcmp.account.page;

import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
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
        col.addComponent(new Segment().addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("找回密码").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(viewHelper.nextForm(form, bindingResult)));
    }
}
