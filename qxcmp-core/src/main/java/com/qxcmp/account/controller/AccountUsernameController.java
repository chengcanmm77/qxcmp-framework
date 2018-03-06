package com.qxcmp.account.controller;

import com.qxcmp.account.*;
import com.qxcmp.account.form.AccountResetUsernameQuestionForm;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.views.Overview;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.qxcmp.core.QxcmpSystemConfig.ACCOUNT_ENABLE_USERNAME;

@RequestMapping("/account/username/")
public class AccountUsernameController extends AccountController {

    private AccountSecurityQuestionService securityQuestionService;

    public AccountUsernameController(AccountService accountService, AccountCodeService codeService, AccountSecurityQuestionService securityQuestionService) {
        super(accountService, codeService, securityQuestionService);
    }


    @PostMapping("reset/question")
    public ModelAndView securityQuestion(@Valid final AccountResetUsernameQuestionForm form, BindingResult bindingResult) {

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

    private boolean validateQuestion(AccountResetUsernameQuestionForm form, AccountSecurityQuestion securityQuestion) {

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
