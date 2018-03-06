package com.qxcmp.account.page;

import com.qxcmp.account.AccountComponent;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.grid.Col;
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
public class ResetSelectPage extends BaseAccountPage {

    private final Collection<AccountComponent> accountComponents;

    @Override
    public void renderContent(Col col) {

        List list = new List().setSelection();
        accountComponents.forEach(accountComponent -> list.addItem(new TextItem(accountComponent.getResetName()).setUrl(accountComponent.getResetUrl())));

        col.addComponent(new Segment().setAlignment(Alignment.CENTER)
                .addComponent(getPageHeader("请选择密码找回方式"))
                .addComponent(list)
                .addComponent(new Divider())
                .addComponent(new Button("返回登录", "/login").setBasic()));
    }
}
