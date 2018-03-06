package com.qxcmp.account.page;

import com.qxcmp.account.AccountComponent;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.list.List;
import com.qxcmp.web.view.elements.list.item.TextItem;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 账户操作选择页面
 * <p>
 * 用于注册方式或找回密码选择
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AccountSelectPage extends BaseAccountPage {

    private final String title;
    private final Collection<AccountComponent> accountComponents;

    @Override
    public void renderContent(Col col) {

        List list = new com.qxcmp.web.view.elements.list.List().setSelection();
        accountComponents.forEach(accountComponent -> list.addItem(new TextItem(accountComponent.getRegisterName()).setUrl(accountComponent.getRegisterUrl())));

        col.addComponent(new Segment().setAlignment(Alignment.CENTER)
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle(title).setDividing().setAlignment(Alignment.LEFT))
                .addComponent(list)
                .addComponent(new Divider())
                .addComponent(new Button("返回登录", "/login").setBasic()));
    }
}
