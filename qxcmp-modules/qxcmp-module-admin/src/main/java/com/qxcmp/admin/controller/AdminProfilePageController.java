package com.qxcmp.admin.controller;

import com.qxcmp.account.AccountSecurityQuestion;
import com.qxcmp.account.AccountSecurityQuestionService;
import com.qxcmp.account.AccountService;
import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.QxcmpAdminModule;
import com.qxcmp.admin.form.*;
import com.qxcmp.admin.page.*;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.html.P;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_PROFILE_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_PROFILE_URL)
@RequiredArgsConstructor
public class AdminProfilePageController extends QxcmpAdminController {

    private final AccountSecurityQuestionService securityQuestionService;
    private final AccountService accountService;

    @GetMapping("/info")
    public ModelAndView infoGet(final AdminProfileInfoForm form, BindingResult bindingResult) {
        User user = userService.currentUser();
        form.setPortrait(user.getPortrait());
        form.setNickname(user.getNickname());
        form.setPersonalizedSignature(user.getPersonalizedSignature());
        return page(AdminProfileInfoPage.class, form, bindingResult);
    }

    @PostMapping("/info")
    public ModelAndView infoPost(@Valid final AdminProfileInfoForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return infoGet(form, bindingResult);
        }
        return execute("修改基本资料", context -> {
            try {
                User currentUser = currentUser().orElseThrow(RuntimeException::new);
                userService.update(currentUser.getId(), user -> userService.mergeToEntity(form, user));
                refreshUser();
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ""));
    }

    @GetMapping("/security")
    public ModelAndView securityPage() {
        User user = userService.currentUser();
        boolean hasSecurityQuestion = securityQuestionService.findByUserId(user.getId()).map(accountSecurityQuestion -> StringUtils.isNotBlank(accountSecurityQuestion.getQuestion1()) && StringUtils.isNotBlank(accountSecurityQuestion.getQuestion2()) && StringUtils.isNotBlank(accountSecurityQuestion.getQuestion3())).orElse(false);
        return page(AdminProfileSecurityPage.class, user, hasSecurityQuestion);
    }

    @GetMapping("/security/password")
    public ModelAndView securityPasswordGet(final AdminProfileSecurityPasswordForm form, BindingResult bindingResult) {
        return page(AdminProfileSecurityPasswordPage.class, form, bindingResult);
    }

    @PostMapping("/security/password")
    public ModelAndView securityPasswordPost(@Valid final AdminProfileSecurityPasswordForm form, BindingResult bindingResult) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        if (!new BCryptPasswordEncoder().matches(form.getOldPassword(), user.getPassword())) {
            bindingResult.rejectValue("oldPassword", "BadCredential");
        }
        if (!StringUtils.equals(form.getNewPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }
        if (bindingResult.hasErrors()) {
            return securityPasswordGet(form, bindingResult);
        }
        return execute("修改登录密码", context -> {
            try {
                userService.update(user.getId(), u -> u.setPassword(new BCryptPasswordEncoder().encode(form.getNewPassword())));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_PROFILE_URL + "/security"));
    }

    @GetMapping("/security/phone")
    public ModelAndView securityPhoneGet(final AdminProfileSecurityPhoneForm form, BindingResult bindingResult) {
        return page(AdminProfileSecurityPhonePage.class, form, bindingResult);
    }

    @PostMapping("/security/phone")
    public ModelAndView securityPhonePost(@Valid final AdminProfileSecurityPhoneForm form, BindingResult bindingResult) {
        userService.findByPhone(form.getPhone()).ifPresent(user -> bindingResult.rejectValue("phone", "Account.bind.phoneExist"));
        verifyCaptcha(form.getCaptcha(), bindingResult);
        if (bindingResult.hasErrors()) {
            return securityPhoneGet(form, bindingResult);
        }
        return execute("手机绑定", context -> {
            try {
                User user = currentUser().orElseThrow(RuntimeException::new);
                userService.update(user.getId(), u -> u.setPhone(form.getPhone()));
                refreshUser();
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_PROFILE_URL + "/security"));
    }

    @GetMapping("/security/email")
    public ModelAndView securityEmailGet(final AdminProfileSecurityEmailForm form, BindingResult bindingResult) {
        return page(AdminProfileSecurityEmailPage.class, form, bindingResult);
    }

    @PostMapping("/security/email")
    public ModelAndView securityEmailPost(@Valid final AdminProfileSecurityEmailForm form, BindingResult bindingResult, final AdminProfileSecurityEmailBindForm bindForm) {
        userService.findByEmail(form.getEmail()).ifPresent(user -> bindingResult.rejectValue("email", "Account.bind.emailExist"));
        verifyCaptcha(form.getCaptcha(), bindingResult);
        if (bindingResult.hasErrors()) {
            return securityEmailGet(form, bindingResult);
        }
        String code = RandomStringUtils.randomAlphanumeric(8);
        accountService.sendBindEmail(form.getEmail(), code);
        request.getSession().setAttribute(QxcmpAdminModule.EMAIL_BINDING_SESSION_ATTR, code);
        request.getSession().setAttribute(QxcmpAdminModule.EMAIL_BINDING_CONTENT_SESSION_ATTR, form.getEmail());
        bindForm.setCaptcha("");
        return page(AdminProfileSecurityEmailBindPage.class, bindForm, bindingResult);
    }

    @PostMapping("/security/email/bind")
    public ModelAndView securityEmailBindPage(@Valid final AdminProfileSecurityEmailBindForm form, BindingResult bindingResult) {
        if (Objects.isNull(request.getSession().getAttribute(QxcmpAdminModule.EMAIL_BINDING_SESSION_ATTR)) || !StringUtils.equals(form.getCaptcha(), (String) request.getSession().getAttribute(QxcmpAdminModule.EMAIL_BINDING_SESSION_ATTR)) || bindingResult.hasErrors()) {
            return overviewPage(viewHelper.nextWarningOverview("邮箱绑定失败").addComponent(new P("绑定验证不正确或者已过期")).addLink("返回", ADMIN_PROFILE_URL + "/security"));
        }
        return execute("邮箱绑定", context -> {
            try {
                User user = currentUser().orElseThrow(RuntimeException::new);
                String email = (String) request.getSession().getAttribute(QxcmpAdminModule.EMAIL_BINDING_CONTENT_SESSION_ATTR);
                userService.update(user.getId(), u -> u.setEmail(email));
                refreshUser();
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_PROFILE_URL + "/security"));
    }

    @GetMapping("/security/question")
    public ModelAndView securityQuestionGet(final AdminProfileSecurityQuestionForm form, BindingResult bindingResult) {
        securityQuestionService.findByUserId(currentUser().map(User::getId).orElse("")).ifPresent(securityQuestion -> {
            securityQuestionService.mergeToObject(securityQuestion, form);
        });
        return page(AdminProfileSecurityQuestionPage.class, form, bindingResult)
                .addObject("selection_items_question1", QxcmpAdminModule.QUESTIONS_LIST_1)
                .addObject("selection_items_question2", QxcmpAdminModule.QUESTIONS_LIST_2)
                .addObject("selection_items_question3", QxcmpAdminModule.QUESTIONS_LIST_3);
    }

    @PostMapping("/security/question")
    public ModelAndView securityQuestionPost(@Valid final AdminProfileSecurityQuestionForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return securityQuestionGet(form, bindingResult);
        }
        return execute("密保设置", context -> {
            try {
                User user = currentUser().orElseThrow(RuntimeException::new);
                AccountSecurityQuestion next = securityQuestionService.next();
                securityQuestionService.mergeToEntity(form, next);
                securityQuestionService.setForUser(user.getId(), next);
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_PROFILE_URL + "/security"));
    }
}
