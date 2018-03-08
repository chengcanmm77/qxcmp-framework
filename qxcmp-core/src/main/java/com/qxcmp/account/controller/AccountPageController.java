package com.qxcmp.account.controller;

import com.qxcmp.account.*;
import com.qxcmp.account.form.*;
import com.qxcmp.account.page.*;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.page.QxcmpOverviewPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.qxcmp.core.QxcmpSystemConfig.*;

/**
 * 账户登录、注册、重置页面路由
 *
 * @author Aaric
 */
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountPageController extends QxcmpController {

    protected final AccountService accountService;
    protected final AccountCodeService codeService;
    private final AccountSecurityQuestionService securityQuestionService;

    @GetMapping("/logon")
    public ModelAndView logon() {
        List<AccountComponent> accountComponents = accountService.getRegisterItems();
        if (accountComponents.isEmpty()) {
            return page(LogonClosePage.class);
        } else if (accountComponents.size() == 1) {
            return redirect(accountComponents.get(0).getRegisterUrl());
        } else {
            return page(LogonSelectPage.class, accountComponents);
        }
    }

    @GetMapping("/logon/username")
    public ModelAndView logonUsername(final AccountLogonUsernameForm form) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).filter(aBoolean -> aBoolean).map(aBoolean -> page(LogonPage.class, form, null)).orElse(page(LogonClosePage.class));
    }

    @PostMapping("/logon/username")
    public ModelAndView logonUsername(@Valid final AccountLogonUsernameForm form, BindingResult bindingResult) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).filter(aBoolean -> aBoolean).map(aBoolean -> {
            verifyCaptcha(form.getCaptcha(), bindingResult);
            accountService.logon(form, bindingResult);
            if (bindingResult.hasErrors()) {
                return page(LogonPage.class, form, bindingResult);
            }
            return page(LogonSuccessPage.class);
        }).orElse(page(LogonClosePage.class));
    }

    @GetMapping("/logon/email")
    public ModelAndView logonEmail(final AccountLogonEmailForm form) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_EMAIL).filter(aBoolean -> aBoolean).map(aBoolean -> page(LogonPage.class, form, null)).orElse(page(LogonClosePage.class));
    }

    @PostMapping("/logon/email")
    public ModelAndView logonEmail(@Valid final AccountLogonEmailForm form, BindingResult bindingResult) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_EMAIL).filter(aBoolean -> aBoolean).map(aBoolean -> {
            verifyCaptcha(form.getCaptcha(), bindingResult);
            accountService.logon(form, bindingResult);
            if (bindingResult.hasErrors()) {
                return page(LogonPage.class, form, bindingResult);
            }
            return page(EmailSendSuccessPage.class);
        }).orElse(page(LogonClosePage.class));
    }

    @GetMapping("/logon/phone")
    public ModelAndView logonPhone(final AccountLogonPhoneForm form) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_PHONE).filter(aBoolean -> aBoolean).map(aBoolean -> page(LogonPage.class, form, null)).orElse(page(LogonClosePage.class));
    }

    @PostMapping("/logon/phone")
    public ModelAndView logonPhone(@Valid final AccountLogonPhoneForm form, BindingResult bindingResult) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_PHONE).filter(aBoolean -> aBoolean).map(aBoolean -> {
            verifyCaptcha(form.getCaptcha(), bindingResult);
            accountService.logon(form, bindingResult);
            if (bindingResult.hasErrors()) {
                return page(LogonPage.class, form, bindingResult);
            }
            return page(LogonSuccessPage.class);
        }).orElse(page(LogonClosePage.class));
    }

    @GetMapping("/reset")
    public ModelAndView reset() {
        List<AccountComponent> accountComponents = accountService.getResetItems();
        if (accountComponents.isEmpty()) {
            return page(ResetClosePage.class);
        } else if (accountComponents.size() == 1) {
            return redirect(accountComponents.get(0).getResetUrl());
        } else {
            return page(ResetSelectPage.class, accountComponents);
        }
    }

    @GetMapping("/reset/{id}")
    public ModelAndView reset(@PathVariable String id, final AccountResetForm form) {
        return codeService.isInvalidCode(id) ?
                page(QxcmpOverviewPage.class, viewHelper.nextWarningOverview("无效的重置链接", "请确认重置链接是否正确").addLink("重新找回密码", "/account/reset").addLink("返回登录", "/login")) :
                page(ResetPage.class, form, null);
    }

    @PostMapping("/reset/{id}")
    public ModelAndView reset(@PathVariable String id, @Valid final AccountResetForm form, BindingResult bindingResult) {
        if (codeService.isInvalidCode(id)) {
            return page(QxcmpOverviewPage.class, viewHelper.nextWarningOverview("无效的重置链接", "请确认重置链接是否正确").addLink("重新找回密码", "/account/reset").addLink("返回登录", "/login"));
        }
        accountService.reset(id, form, bindingResult);
        if (bindingResult.hasErrors()) {
            return page(ResetPage.class, form, bindingResult);
        }
        return page(QxcmpOverviewPage.class, viewHelper.nextSuccessOverview("密码重置成功", "请使用新的密码登录").addLink("现在去登录", "/login"));
    }

    @GetMapping("/reset/username")
    public ModelAndView resetUsername(final AccountResetUsernameForm form) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).filter(aBoolean -> aBoolean).map(aBoolean -> page(ResetPage.class, form, null)).orElse(page(ResetClosePage.class));
    }

    @PostMapping("/reset/username")
    public ModelAndView resetUsername(@Valid final AccountResetUsernameForm form, BindingResult bindingResult) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).filter(aBoolean -> aBoolean).map(aBoolean -> {
            AccountSecurityQuestion securityQuestion = accountService.getUserSecurityQuestion(form, bindingResult);
            verifyCaptcha(form.getCaptcha(), bindingResult);
            if (bindingResult.hasErrors()) {
                return page(ResetPage.class, form, bindingResult);
            }
            final AccountResetUsernameQuestionForm questionForm = new AccountResetUsernameQuestionForm();
            questionForm.setQuestion1(securityQuestion.getQuestion1());
            questionForm.setQuestion2(securityQuestion.getQuestion2());
            questionForm.setQuestion3(securityQuestion.getQuestion3());
            questionForm.setUserId(securityQuestion.getUserId());
            return page(ResetPage.class, questionForm, null).addObject(questionForm);
        }).orElse(page(ResetClosePage.class));
    }

    @PostMapping("/reset/username/question")
    public ModelAndView resetUsernameQuestion(@Valid final AccountResetUsernameQuestionForm form) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).filter(aBoolean -> aBoolean).map(aBoolean -> {
            AccountCode accountCode = accountService.validateSecurityQuestion(form);
            if (Objects.isNull(accountCode)) {
                return page(QxcmpOverviewPage.class, viewHelper.nextWarningOverview("密保问题回答不正确").addLink("返回", "/account/reset"));
            }
            return page(QxcmpOverviewPage.class, viewHelper.nextSuccessOverview("密保问题验证成功").addLink("重置密码", "/account/reset/" + accountCode.getId()));
        }).orElse(page(ResetClosePage.class));
    }

    @GetMapping("/reset/email")
    public ModelAndView resetEmail(final AccountResetEmailForm form) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_EMAIL).filter(aBoolean -> aBoolean).map(aBoolean -> page(ResetPage.class, form, null)).orElse(page(ResetClosePage.class));
    }

    @PostMapping("/reset/email")
    public ModelAndView resetEmail(@Valid final AccountResetEmailForm form, BindingResult bindingResult) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_EMAIL).filter(aBoolean -> aBoolean).map(aBoolean -> {
            accountService.reset(form, bindingResult);
            verifyCaptcha(form.getCaptcha(), bindingResult);
            if (bindingResult.hasErrors()) {
                return page(ResetPage.class, form, bindingResult);
            }
            return page(QxcmpOverviewPage.class, viewHelper.nextSuccessOverview("密码重置邮件发送成功", "请前往您的邮箱点击重置链接重置密码").addLink("返回登录", "/login"));
        }).orElse(page(ResetClosePage.class));
    }

    @GetMapping("/reset/phone")
    public ModelAndView resetPhone(final AccountResetPhoneForm form) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_PHONE).filter(aBoolean -> aBoolean).map(aBoolean -> page(ResetPage.class, form, null)).orElse(page(ResetClosePage.class));
    }

    @PostMapping("/reset/phone")
    public ModelAndView resetPhone(@Valid final AccountResetPhoneForm form, BindingResult bindingResult) {
        return systemConfigService.getBoolean(ACCOUNT_ENABLE_PHONE).filter(aBoolean -> aBoolean).map(aBoolean -> {
            AccountCode accountCode = accountService.reset(form, bindingResult);
            verifyCaptcha(form.getCaptcha(), bindingResult);
            if (bindingResult.hasErrors()) {
                return page(ResetPage.class, form, bindingResult);
            }
            return page(QxcmpOverviewPage.class, viewHelper.nextSuccessOverview("短信验证成功").addLink("重置密码", "/account/reset/" + accountCode.getId()));
        }).orElse(page(ResetClosePage.class));
    }

    @GetMapping("/activate")
    public ModelAndView activate(final AccountActivateForm form) {
        return page(ActivatePage.class, form, null);
    }

    @PostMapping("/activate")
    public ModelAndView activate(@Valid final AccountActivateForm form, BindingResult bindingResult) {
        accountService.activate(form, bindingResult);
        verifyCaptcha(form.getCaptcha(), bindingResult);
        if (bindingResult.hasErrors()) {
            return page(ActivatePage.class, form, bindingResult);
        }
        return page(QxcmpOverviewPage.class, viewHelper.nextSuccessOverview("激活邮件发送成功", "请前往您的邮箱点击激活链接以激活您的账户").addLink("返回登录", "/login"));
    }

    @GetMapping("/activate/{id}")
    public ModelAndView activate(@PathVariable String id) {
        try {
            accountService.activate(id);
            return page(QxcmpOverviewPage.class, viewHelper.nextSuccessOverview("账户激活成功", "现在可以登录您的账户了").addLink("立即登录", "/login"));
        } catch (Exception e) {
            return page(QxcmpOverviewPage.class, viewHelper.nextWarningOverview("无效的激活链接", "请确认激活链接是否正确").addLink("重新激活账户", "/account/activate").addLink("返回登录", "/login"));
        }
    }
}
