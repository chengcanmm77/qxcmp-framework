package com.qxcmp.account.controller;

import com.qxcmp.account.*;
import com.qxcmp.account.form.AccountLogonUsernameForm;
import com.qxcmp.account.form.AccountUsernameResetForm;
import com.qxcmp.account.form.AccountUsernameResetQuestionForm;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

import static com.qxcmp.core.QxcmpSystemConfig.ACCOUNT_ENABLE_USERNAME;

@RequestMapping("/account/username/")
public class AccountUsernameController extends AccountController {

    private final AccountSecurityQuestionService securityQuestionService;

    public AccountUsernameController(AccountService accountService, AccountCodeService codeService, AccountSecurityQuestionService securityQuestionService) {
        super(accountService, codeService);
        this.securityQuestionService = securityQuestionService;
    }

    @PostMapping("logon")
    public ModelAndView logonEmailPost(@Valid AccountLogonUsernameForm form, BindingResult bindingResult) {

        if (!systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).orElse(false)) {
            return logonClosedPage().build();
        }

        if (userService.findById(form.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "Username.exist");
        }

        if (!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("用户名注册").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).addObject(form)
                    .build();
        }

        try {
            userService.create(() -> {
                User user = userService.next();
                user.setUsername(form.getUsername());
                user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
                user.setAccountNonExpired(true);
                user.setAccountNonLocked(true);
                user.setCredentialsNonExpired(true);
                user.setEnabled(true);
                userService.setDefaultPortrait(user);
                return user;
            });

            return page(new Overview("注册成功", "你已经成功注册账号，现在可以登录了").addLink("立即登录", "/login")).build();
        } catch (Exception e) {
            return page(new Overview(new IconHeader("注册失败", new Icon("warning circle"))).addComponent(new P(e.getMessage())).addLink("返回登录", "/login")).build();
        }
    }

    @GetMapping("reset")
    public ModelAndView reset(final AccountUsernameResetForm form) {

        if (!systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).orElse(false)) {
            return resetClosedPage().build();
        }

        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("密保找回密码").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).build();
    }

    @PostMapping("reset")
    public ModelAndView reset(@Valid final AccountUsernameResetForm form, BindingResult bindingResult) {

        if (!systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).orElse(false)) {
            return resetClosedPage().build();
        }

        Optional<User> userOptional = userService.findById(form.getUsername());

        if (!userOptional.isPresent()) {
            bindingResult.rejectValue("username", "Account.reset.noUsername");
        } else {
            if (!securityQuestionService.findByUserId(userOptional.get().getId()).isPresent()) {
                bindingResult.rejectValue("username", "Account.reset.noQuestion");
            }
        }

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("密保找回密码").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).build();
        }

        Optional<AccountSecurityQuestion> securityQuestionOptional = securityQuestionService.findByUserId(userOptional.get().getId());

        final AccountUsernameResetQuestionForm accountUsernameResetQuestionForm = new AccountUsernameResetQuestionForm();

        accountUsernameResetQuestionForm.setQuestion1(securityQuestionOptional.get().getQuestion1());
        accountUsernameResetQuestionForm.setQuestion2(securityQuestionOptional.get().getQuestion2());
        accountUsernameResetQuestionForm.setQuestion3(securityQuestionOptional.get().getQuestion3());
        accountUsernameResetQuestionForm.setUserId(securityQuestionOptional.get().getUserId());

        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("密保找回密码").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(accountUsernameResetQuestionForm))
        ).addObject(accountUsernameResetQuestionForm).build();
    }

    @PostMapping("reset/question")
    public ModelAndView securityQuestion(@Valid final AccountUsernameResetQuestionForm form, BindingResult bindingResult) {

        if (!systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).orElse(false)) {
            return resetClosedPage().build();
        }

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("密保找回密码").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).build();
        }

        return securityQuestionService.findByUserId(form.getUserId()).map(securityQuestion -> {
            if (validateQuestion(form, securityQuestion)) {
                AccountCode code = codeService.nextPasswordCode(securityQuestion.getUserId());
                return redirect("/account/reset/" + code.getId());
            } else {
                return page(new Overview("找回密码", "密保问题错误").addLink("重新找回密码", "/account/username/reset")).build();
            }
        }).orElse(page(new Overview(new IconHeader("找回密码", new Icon("warning circle")).setSubTitle("无法找到用户信息")).addLink("返回登录", "/login")).build());
    }

    private boolean validateQuestion(AccountUsernameResetQuestionForm form, AccountSecurityQuestion securityQuestion) {

        int count = 0;

        if (form.getAnswer1().equals(securityQuestion.getAnswer1())) {
            count++;
        }

        if (form.getAnswer2().equals(securityQuestion.getAnswer2())) {
            count++;
        }

        if (form.getAnswer3().equals(securityQuestion.getAnswer3())) {
            count++;
        }

        return count >= 2;
    }
}
