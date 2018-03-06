package com.qxcmp.account.controller;

import com.qxcmp.account.AccountCode;
import com.qxcmp.account.AccountCodeService;
import com.qxcmp.account.AccountService;
import com.qxcmp.account.form.AccountActivateForm;
import com.qxcmp.account.form.AccountResetForm;
import com.qxcmp.account.page.LogonClosePage;
import com.qxcmp.account.page.LogonSelectPage;
import com.qxcmp.account.page.ResetClosePage;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.page.AbstractPage;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.divider.Divider;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.list.List;
import com.qxcmp.web.view.elements.list.item.TextItem;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 账户登录、注册、重置页面路由
 *
 * @author Aaric
 */
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController extends QxcmpController {

    protected final AccountService accountService;
    protected final AccountCodeService codeService;

    @GetMapping("/logon")
    public ModelAndView logon() {
        if (accountService.getRegisterItems().isEmpty()) {
            return qxcmpPage(LogonClosePage.class);
        } else if (accountService.getRegisterItems().size() == 1) {
            return redirect(accountService.getRegisterItems().get(0).getRegisterUrl());
        } else {
            return qxcmpPage(LogonSelectPage.class, accountService.getRegisterItems());
        }
    }

    @GetMapping("/reset")
    public ModelAndView reset() {
        if (accountService.getResetItems().isEmpty()) {
            return qxcmpPage(ResetClosePage.class);
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

    @GetMapping("/reset/{id}")
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

    @PostMapping("/reset/{id}")
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

    @GetMapping("/activate")
    public ModelAndView activate(final AccountActivateForm form) {
        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, siteService.getTitle()).setImage(new Image(siteService.getLogo())).setSubTitle("激活账户").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).addObject(form)
                .build();
    }

    @PostMapping("/activate")
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

    @GetMapping("/activate/{id}")
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
