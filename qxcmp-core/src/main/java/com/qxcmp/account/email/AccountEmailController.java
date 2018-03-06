package com.qxcmp.account.email;

import com.qxcmp.account.AccountCodeService;
import com.qxcmp.account.AccountService;
import com.qxcmp.account.controller.AccountPageController;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.views.Overview;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/account/email/")
public class AccountEmailController extends AccountPageController {

    public AccountEmailController(AccountService accountService, AccountCodeService codeService) {
        super(accountService, codeService);
    }

    @GetMapping("logon")
    public ModelAndView logon(final AccountEmailLogonForm form) {

        if (!systemConfigService.getBoolean(QxcmpSystemConfig.ACCOUNT_ENABLE_EMAIL).orElse(false)) {
            return logonClosedPage().build();
        }

        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("邮箱注册").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).build();
    }

    @PostMapping("logon")
    public ModelAndView logonEmailPost(@Valid final AccountEmailLogonForm form, BindingResult bindingResult) {

        if (!systemConfigService.getBoolean(QxcmpSystemConfig.ACCOUNT_ENABLE_EMAIL).orElse(false)) {
            return logonClosedPage().build();
        }

        if (userService.findById(form.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "Username.exist");
        }

        if (userService.findById(form.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "Email.exist");
        }

        if (!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("邮箱注册").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).build();
        }

        try {
            accountService.sendActivateEmail(
                    userService.create(() -> {
                        User user = userService.next();
                        user.setUsername(form.getUsername());
                        user.setEmail(form.getEmail());
                        user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
                        user.setAccountNonExpired(true);
                        user.setAccountNonLocked(true);
                        user.setCredentialsNonExpired(true);
                        user.setEnabled(false);
                        userService.setDefaultPortrait(user);
                        return user;
                    }));

            return page(new Overview("注册成功", "激活邮件已经发送到您的邮件，请前往激活。如果您未收到激活邮件，请检查是否被黑名单过滤，或者再次重新发送激活邮件").addLink("立即登录", "/login")).build();
        } catch (Exception e) {
            return page(new Overview(new IconHeader("注册失败", new Icon("warning circle"))).addComponent(new P(e.getMessage())).addLink("返回登录", "/login")).build();
        }
    }

    @GetMapping("reset")
    public ModelAndView reset(final AccountEmailResetForm form) {

        if (!systemConfigService.getBoolean(QxcmpSystemConfig.ACCOUNT_ENABLE_EMAIL).orElse(false)) {
            return resetClosedPage().build();
        }

        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("邮箱找回密码").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).build();
    }

    @PostMapping("reset")
    public ModelAndView resetEmailPost(@Valid final AccountEmailResetForm form, BindingResult bindingResult) {

        if (!systemConfigService.getBoolean(QxcmpSystemConfig.ACCOUNT_ENABLE_EMAIL).orElse(false)) {
            return resetClosedPage().build();
        }

        Optional<User> userOptional = userService.findByEmail(form.getEmail());

        if (!userOptional.isPresent()) {
            bindingResult.rejectValue("email", "Account.reset.noEmail");
        }

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("邮箱找回密码").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).build();
        }

        try {
            accountService.sendResetEmail(userOptional.get());
            return page(new Overview("密码重置邮件发送成功", "请前往您的邮箱点击重置链接重置密码").addLink("返回登录", "/login")).build();
        } catch (Exception e) {
            return page(new Overview(new IconHeader("密码重置邮件发送失败", new Icon("warning circle"))).addComponent(new P(e.getMessage())).addLink("返回登录", "/login")).build();
        }
    }
}
