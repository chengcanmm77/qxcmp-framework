package com.qxcmp.account;

import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.auth.AuthenticationFailureHandler;
import com.qxcmp.web.page.AbstractPage;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.container.Container;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.divider.HorizontalDivider;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.list.List;
import com.qxcmp.web.view.elements.list.item.TextItem;
import com.qxcmp.web.view.elements.message.ErrorMessage;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.ColumnCount;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static com.qxcmp.web.auth.AuthenticationFailureHandler.AUTHENTICATION_ERROR_MESSAGE;

/**
 * 账户登录、注册、重置页面路由
 *
 * @author Aaric
 */
@Controller
@RequiredArgsConstructor
public class AccountPageController extends QxcmpController {

    protected final AccountService accountService;
    protected final AccountCodeService codeService;

    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam(required = false) String callback, final LoginForm loginForm, final LoginFormWithCaptcha loginFormWithCaptcha) {

        return buildPage(segment -> {

            loginForm.setCallback(callback);
            loginFormWithCaptcha.setCallback(callback);

            boolean showCaptcha = false;

            if (request.getSession().getAttribute(AuthenticationFailureHandler.AUTHENTICATION_SHOW_CAPTCHA) != null) {
                showCaptcha = (boolean) request.getSession().getAttribute(AuthenticationFailureHandler.AUTHENTICATION_SHOW_CAPTCHA);
            }

            segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setDividing())
                    .addComponent(convertToForm(showCaptcha ? loginFormWithCaptcha : loginForm).setErrorMessage(getLoginErrorMessage()))
                    .addComponent(new HorizontalDivider("或"))
                    .addComponent(new Container().setAlignment(Alignment.CENTER).addComponent(new Anchor("注册新用户", "/account/logon")).addComponent(new Anchor("忘记密码?", "/account/reset")));
        }).build();
    }

    @GetMapping("/account/logon")
    public ModelAndView logon() {
        if (accountService.getRegisterItems().isEmpty()) {
            return logonClosedPage().build();
        } else if (accountService.getRegisterItems().size() == 1) {
            return redirect(accountService.getRegisterItems().get(0).getRegisterUrl());
        } else {
            List list = new List().setSelection();
            accountService.getRegisterItems().forEach(accountComponent -> list.addItem(new TextItem(accountComponent.getRegisterName()).setUrl(accountComponent.getRegisterUrl())));
            return buildPage(segment -> segment.setAlignment(Alignment.CENTER)
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("请选择注册方式").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(list)
                    .addComponent(new Divider())
                    .addComponent(new Button("返回登录", "/login").setBasic())
            ).build();
        }
    }

    @GetMapping("/account/reset")
    public ModelAndView reset() {
        if (accountService.getResetItems().isEmpty()) {
            return resetClosedPage().build();
        } else if (accountService.getResetItems().size() == 1) {
            return redirect(accountService.getResetItems().get(0).getResetUrl());
        } else {
            List list = new List().setSelection();
            accountService.getRegisterItems().stream().filter(accountComponent -> !accountComponent.isDisableReset()).forEach(accountComponent -> list.addItem(new TextItem(accountComponent.getResetName()).setUrl(accountComponent.getResetUrl())));
            return buildPage(segment -> segment.setAlignment(Alignment.CENTER)
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("请选择密码找回方式").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(list)
                    .addComponent(new Divider())
                    .addComponent(new Button("返回登录", "/login").setBasic())
            ).build();
        }
    }

    @GetMapping("/account/reset/{id}")
    public ModelAndView reset(@PathVariable String id, final AccountResetForm form) {

        if (codeService.isInvalidCode(id)) {
            return page(new Overview(new IconHeader("无效的重置链接", new Icon("warning circle")).setSubTitle("请确认重置链接是否正确，或者重新找回密码")).addLink("重新找回密码", "/account/reset").addLink("返回登录", "/login")).build();
        }

        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle(String.format("为用户 %s 找回密码", getResetUsername(id))).setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).addObject(form)
                .build();
    }

    @PostMapping("/account/reset/{id}")
    public ModelAndView reset(@PathVariable String id, @Valid final AccountResetForm form, BindingResult bindingResult) throws Exception {

        AccountCode code = codeService.findOne(id).orElse(null);

        if (codeService.isInvalidCode(id)) {
            return page(new Overview(new IconHeader("无效的重置链接", new Icon("warning circle")).setSubTitle("请确认重置链接是否正确，或者重新找回密码")).addLink("重新找回密码", "/account/reset").addLink("返回登录", "/login")).build();
        }

        if (!StringUtils.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle(String.format("为用户 %s 找回密码", getResetUsername(id))).setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).addObject(form)
                    .build();
        }

        userService.update(code.getUserId(), user -> {
            user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
            codeService.delete(code);
        });

        return page(new Overview("密码重置成功", "请使用新的密码登录").addLink("现在去登录", "/login")).build();
    }

    @GetMapping("/account/activate")
    public ModelAndView activate(final AccountActivateForm form) {
        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("激活账户").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).addObject(form)
                .build();
    }

    @PostMapping("/account/activate")
    public ModelAndView activate(@Valid final AccountActivateForm form, BindingResult bindingResult) {

        Optional<User> userOptional = userService.findByUsername(form.getUsername());

        if (!userOptional.isPresent()) {
            bindingResult.rejectValue("username", "Account.activate.notExist");
        } else {
            if (StringUtils.isBlank(userOptional.get().getEmail())) {
                bindingResult.rejectValue("username", "Account.activate.noEmail");
            }

            if (userOptional.get().isEnabled()) {
                bindingResult.rejectValue("username", "Account.activate.activated");
            }
        }

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("激活账户").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).addObject(form)
                    .build();
        }

        try {
            accountService.sendActivateEmail(userOptional.get());

            return buildPage(segment -> segment.setAlignment(Alignment.CENTER)
                    .addComponent(new PageHeader(HeaderType.H2, "发送激活邮件成功").setSubTitle("激活邮件已经发送到您的邮件，请前往激活。如果您未收到激活邮件，请检查是否被黑名单过滤，或者再次重新发送激活邮件"))
                    .addComponent(new Divider())
                    .addComponent(new Button("立即登录", "/login").setBasic())
            ).build();
        } catch (Exception e) {
            return page(new Overview("发送激活邮件失败").addComponent(new P(e.getMessage())).addLink("重新发送", "/account/activate").addLink("返回登录", "/login")).build();
        }
    }

    @GetMapping("/account/activate/{id}")
    public ModelAndView activate(@PathVariable String id) {
        try {
            AccountCode code = codeService.findOne(id).orElseThrow(Exception::new);

            if (codeService.isInvalidCode(id) || !code.getType().equals(AccountCode.Type.ACTIVATE)) {
                return page(new Overview(new IconHeader("账户激活失败", new Icon("warning circle")).setSubTitle("无效的激活码")).addLink("重新激活账户", "/account/activate").addLink("返回登录", "/login")).build();
            }

            userService.update(code.getUserId(), user -> {
                user.setEnabled(true);
                codeService.delete(code);
            });

            return page(new Overview("账户激活成功", "现在可以登录您的账户了").addLink("现在去登录", "/login")).build();
        } catch (Exception e) {
            return page(new Overview(new IconHeader("账户激活失败", new Icon("warning circle")).setSubTitle("无效的激活码")).addLink("重新激活账户", "/account/activate").addLink("返回登录", "/login")).build();
        }
    }

    protected AbstractPage buildPage(Consumer<Segment> consumer) {
        Segment segment = new Segment();
        consumer.accept(segment);
        return page().addComponent(new VerticallyDividedGrid().setVerticallyPadded().setTextContainer().setColumnCount(ColumnCount.ONE).addItem(new Col().addComponent(segment)));
    }

    protected AbstractPage logonClosedPage() {
        return page(new Overview(new IconHeader("注册功能已经关闭", new Icon("warning circle")).setSubTitle("请在注册功能开放时进行注册")).addLink("返回登录", "/login"));
    }

    protected AbstractPage resetClosedPage() {
        return page(new Overview(new IconHeader("密码找回功能已经关闭", new Icon("warning circle")).setSubTitle("请与平台管理员联系")).addLink("返回登录", "/login"));
    }

    private ErrorMessage getLoginErrorMessage() {

        if (Objects.nonNull(request.getSession().getAttribute(AUTHENTICATION_ERROR_MESSAGE))) {
            return (ErrorMessage) new ErrorMessage("登录失败", applicationContext.getMessage(request.getSession().getAttribute(AUTHENTICATION_ERROR_MESSAGE).toString(), null, null)).setCloseable();
        }

        return null;
    }

    private String getResetUsername(String codeId) {
        return userService.findOne(codeService.findOne(codeId).orElse(null).getUserId()).map(user -> {
            if (StringUtils.isNotBlank(user.getUsername())) {
                return user.getUsername();
            }

            if (StringUtils.isNotBlank(user.getEmail())) {
                return user.getEmail();
            }

            if (StringUtils.isNotBlank(user.getPhone())) {
                return user.getPhone();
            }

            return user.getId();
        }).orElse("未知用户");
    }
}
