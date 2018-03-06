package com.qxcmp.account.page;

import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 账户重置关闭页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class ResetClosePage extends BaseAccountPage {
    @Override
    public void renderContent(Col col) {
        col.addComponent(viewHelper.nextOverview(new Icon("warning circle").setColor(Color.ORANGE), "密码找回功能已经关闭", "请等待功能开放").addLink("返回登录", "/login"));
    }
}
