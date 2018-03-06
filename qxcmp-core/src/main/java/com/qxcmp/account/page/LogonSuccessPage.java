package com.qxcmp.account.page;

import com.qxcmp.web.view.elements.grid.Col;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 账户注册成功页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class LogonSuccessPage extends BaseAccountPage {
    @Override
    public void renderContent(Col col) {
        col.addComponent(viewHelper.nextSuccessOverview("注册成功", "现在可以登录了").addLink("立即登录", "/login"));
    }
}
