package com.qxcmp.account.page;

import com.qxcmp.account.form.LoginForm;
import com.qxcmp.account.form.LoginFormWithCaptcha;
import com.qxcmp.web.view.elements.container.Container;
import com.qxcmp.web.view.elements.divider.HorizontalDivider;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.message.ErrorMessage;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.qxcmp.account.auth.AuthenticationFailureHandler.AUTHENTICATION_ERROR_MESSAGE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 平台登录页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class LoginPage extends BaseAccountPage {

    private final HttpServletRequest request;
    private final LoginForm loginForm;
    private final LoginFormWithCaptcha loginFormWithCaptcha;
    private final boolean showCaptcha;

    @Override
    public void renderContent(Col col) {
        col.addComponent(new Segment()
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setDividing())
                .addComponent(viewHelper.nextForm(showCaptcha ? loginFormWithCaptcha : loginForm).setErrorMessage(getLoginErrorMessage()))
                .addComponent(new HorizontalDivider("或"))
                .addComponent(new Container().setAlignment(Alignment.CENTER)
                        .addComponent(new Anchor("注册新用户", "/account/logon"))
                        .addComponent(new Anchor("忘记密码?", "/account/reset"))));
    }

    private ErrorMessage getLoginErrorMessage() {

        if (Objects.nonNull(request.getSession().getAttribute(AUTHENTICATION_ERROR_MESSAGE))) {
            return (ErrorMessage) new ErrorMessage("登录失败", applicationContext.getMessage(request.getSession().getAttribute(AUTHENTICATION_ERROR_MESSAGE).toString(), null, null)).setCloseable();
        }

        return null;
    }
}
