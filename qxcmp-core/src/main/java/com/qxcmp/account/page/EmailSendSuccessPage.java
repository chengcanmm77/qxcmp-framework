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
public class EmailSendSuccessPage extends BaseAccountPage {
    @Override
    public void renderContent(Col col) {
        col.addComponent(viewHelper.nextSuccessOverview("注册成功", "激活邮件已经发送到您的邮件，请前往激活。如果您未收到激活邮件，请检查是否被黑名单过滤，或者再次重新发送激活邮件").addLink("重新发送激活邮件", "/account/active").addLink("立即登录", "/login"));
    }
}
