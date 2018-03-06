package com.qxcmp.web.controller;

import com.qxcmp.account.AccountService;
import com.qxcmp.account.AccountSecurityQuestion;
import com.qxcmp.account.AccountSecurityQuestionService;
import com.qxcmp.audit.ActionException;
import com.qxcmp.message.InnerMessage;
import com.qxcmp.message.InnerMessageService;
import com.qxcmp.user.User;
import com.qxcmp.util.ProfilePageHelper;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.*;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.html.HtmlText;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.modules.table.*;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/profile")
@RequiredArgsConstructor
public class AdminProfilePageController extends QxcmpController {

    private final AccountSecurityQuestionService securityQuestionService;
    private final AccountService accountService;
    private final InnerMessageService innerMessageService;

    @GetMapping("/message")
    public ModelAndView messagePage(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<InnerMessage> messages = innerMessageService.findByUserID(user.getId(), pageable);
        return page().addComponent(convertToTable(InnerMessage.class, messages))
                .setBreadcrumb("控制台", "", "个人中心", null, "站内消息")
                .build();
    }

    @GetMapping("/message/{id}/details")
    public ModelAndView messageDetailsPage(@PathVariable String id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return innerMessageService.findOne(id)
                .filter(innerMessage -> StringUtils.equals(innerMessage.getUserId(), user.getId()))
                .map(innerMessage -> {

                    innerMessageService.update(innerMessage.getId(), message -> message.setUnread(false));

                    return page()
                            .addComponent(new Overview(innerMessage.getTitle(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(innerMessage.getSendTime()))
                                    .addComponent(new HtmlText(innerMessage.getContent()))
                                    .addLink("返回我的站内信", QXCMP_BACKEND_URL + "/profile/message"))
                            .setBreadcrumb("控制台", "", "个人中心", null, "站内消息", "profile/message", innerMessage.getTitle())
                            .build();
                })
                .orElse(page(viewHelper.nextWarningOverview("站内消息不存在", "")).build());
    }

    @PostMapping("/message/{id}/read")
    public ResponseEntity<RestfulResponse> messageRead(@PathVariable String id) {
        return innerMessageService.findOne(id)
                .map(innerMessage -> {
                    RestfulResponse restfulResponse = audit("标记站内信为已读", context -> {
                        try {
                            innerMessageService.update(innerMessage.getId(), message -> message.setUnread(false));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @PostMapping("/message/{id}/remove")
    public ResponseEntity<RestfulResponse> messageRemove(@PathVariable String id) {
        return innerMessageService.findOne(id)
                .map(innerMessage -> {
                    RestfulResponse restfulResponse = audit("删除站内信", context -> {
                        try {
                            innerMessageService.delete(innerMessage);
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @GetMapping("/info")
    public ModelAndView infoPage(final AdminProfileInfoForm form) {
        User user = userService.currentUser();
        form.setPortrait(user.getPortrait());
        form.setNickname(user.getNickname());
        form.setPersonalizedSignature(user.getPersonalizedSignature());

        return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "个人中心", null, "基本资料")
                .build();
    }

    @PostMapping("/info")
    public ModelAndView infoPage(@Valid final AdminProfileInfoForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "基本资料")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                User user = userService.currentUser();
                userService.update(user.getId(), u -> {
                    u.setPortrait(form.getPortrait());
                    u.setNickname(form.getNickname());
                    u.setPersonalizedSignature(form.getPersonalizedSignature());
                });
                refreshUser();
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/profile/info"));
    }

    @GetMapping("/security")
    public ModelAndView securityPage() {
        User user = userService.currentUser();

        boolean hasSecurityQuestion = securityQuestionService.findByUserId(user.getId()).map(accountSecurityQuestion -> StringUtils.isNotBlank(accountSecurityQuestion.getQuestion1()) && StringUtils.isNotBlank(accountSecurityQuestion.getQuestion2()) && StringUtils.isNotBlank(accountSecurityQuestion.getQuestion3())).orElse(false);

        return page().addComponent(new Segment()
                .addComponent(new PageHeader(HeaderType.H2, "安全设置").setDividing())
                .addComponent(new Table().setBasic().setHeader((AbstractTableHeader) new TableHeader()
                        .addRow(new TableRow().addCell(new TableHead("登录密码").setAlignment(Alignment.CENTER)).addCell(new TableData("安全性高的密码可以使帐号更安全")).addCell(new TableData(StringUtils.isNotBlank(user.getPassword()) ? new Icon("check circle").setColor(Color.GREEN) : new Icon("warning circle").setColor(Color.ORANGE)).setAlignment(Alignment.CENTER)).addCell(new TableData(new Button("修改", QXCMP_BACKEND_URL + "/profile/security/password").setBasic()).setAlignment(Alignment.CENTER)))
                        .addRow(new TableRow().addCell(new TableHead("支付密码").setAlignment(Alignment.CENTER)).addCell(new TableData("支付密码可以提高在支付场合的安全性")).addCell(new TableData(StringUtils.isNotBlank(user.getPayPassword()) ? new Icon("check circle").setColor(Color.GREEN) : new Icon("warning circle").setColor(Color.ORANGE)).setAlignment(Alignment.CENTER)).addCell(new TableData(new Button(StringUtils.isNotBlank(user.getPayPassword()) ? "修改" : "设置", QXCMP_BACKEND_URL + (StringUtils.isNotBlank(user.getPayPassword()) ? "/profile/security/pay/password/edit" : "/profile/security/pay/password")).setBasic()).setAlignment(Alignment.CENTER)))
                        .addRow(new TableRow().addCell(new TableHead("手机绑定").setAlignment(Alignment.CENTER)).addCell(new TableData("手机号可以直接用于登录、找回密码等")).addCell(new TableData(StringUtils.isNotBlank(user.getPhone()) ? new Icon("check circle").setColor(Color.GREEN) : new Icon("warning circle").setColor(Color.ORANGE)).setAlignment(Alignment.CENTER)).addCell(new TableData(new Button("绑定", QXCMP_BACKEND_URL + "/profile/security/phone").setBasic()).setAlignment(Alignment.CENTER)))
                        .addRow(new TableRow().addCell(new TableHead("邮箱绑定").setAlignment(Alignment.CENTER)).addCell(new TableData("邮箱可以直接用于登录、找回密码等")).addCell(new TableData(StringUtils.isNotBlank(user.getEmail()) ? new Icon("check circle").setColor(Color.GREEN) : new Icon("warning circle").setColor(Color.ORANGE)).setAlignment(Alignment.CENTER)).addCell(new TableData(new Button("绑定", QXCMP_BACKEND_URL + "/profile/security/email").setBasic()).setAlignment(Alignment.CENTER)))
                        .addRow(new TableRow().addCell(new TableHead("密保问题").setAlignment(Alignment.CENTER)).addCell(new TableData("建议设置三个容易记住，且最不容易被他人获取的问题及答案，更有效保障您的密码安全")).addCell(new TableData(hasSecurityQuestion ? new Icon("check circle").setColor(Color.GREEN) : new Icon("warning circle").setColor(Color.ORANGE)).setAlignment(Alignment.CENTER)).addCell(new TableData(new Button("设置", QXCMP_BACKEND_URL + "/profile/security/question").setBasic()).setAlignment(Alignment.CENTER)))
                )))
                .setBreadcrumb("控制台", "", "个人中心", null, "安全设置")
                .build();
    }

    @GetMapping("/security/password")
    public ModelAndView securityPasswordPage(final AdminProfileSecurityPasswordForm form) {
        return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "修改登录密码")
                .build();
    }

    @PostMapping("/security/password")
    public ModelAndView securityPasswordPage(@Valid final AdminProfileSecurityPasswordForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        if (!new BCryptPasswordEncoder().matches(form.getOldPassword(), user.getPassword())) {
            bindingResult.rejectValue("oldPassword", "BadCredential");
        }

        if (!StringUtils.equals(form.getNewPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "修改登录密码")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                userService.update(user.getId(), u -> u.setPassword(new BCryptPasswordEncoder().encode(form.getNewPassword())));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/profile/security"));
    }

    @GetMapping("/security/pay/password")
    public ModelAndView securityPayPasswordPage(final AdminProfileSecurityPayPasswordForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        checkState(StringUtils.isBlank(user.getPayPassword()), "已经设置了支付密码");

        return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "设置支付密码")
                .build();
    }

    @PostMapping("/security/pay/password")
    public ModelAndView securityPayPasswordPage(@Valid final AdminProfileSecurityPayPasswordForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        if (!StringUtils.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "设置支付密码")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                User u = userService.update(user.getId(), user1 -> {
                    user1.setPayPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
                    user1.getPasswordHistory().add(user1.getPayPassword());
                });

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities()));

            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/profile/security"));
    }

    @GetMapping("/security/pay/password/edit")
    public ModelAndView securityPayPasswordEditPage(final AdminProfileSecurityPayPasswordEditForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        checkState(StringUtils.isNotBlank(user.getPayPassword()), "尚未设置支付密码");

        return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "修改支付密码")
                .build();
    }

    @PostMapping("/security/pay/password/edit")
    public ModelAndView securityPayPasswordEditPage(@Valid final AdminProfileSecurityPayPasswordEditForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        if (!new BCryptPasswordEncoder().matches(form.getOldPassword(), user.getPayPassword())) {
            bindingResult.rejectValue("oldPassword", "BadCredential");
        }

        if (!StringUtils.equals(form.getNewPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "修改支付密码")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                User u = userService.update(user.getId(), user1 -> {
                    user1.setPayPassword(new BCryptPasswordEncoder().encode(form.getNewPassword()));
                    user1.getPasswordHistory().add(user1.getPayPassword());
                });

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities()));

            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/profile/security"));
    }


    @GetMapping("/security/phone")
    public ModelAndView securityPhonePage(final AdminProfileSecurityPhoneForm form) {
        return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "手机绑定")
                .build();
    }

    @PostMapping("/security/phone")
    public ModelAndView securityPhonePage(@Valid final AdminProfileSecurityPhoneForm form, BindingResult bindingResult) {

        userService.findByPhone(form.getPhone()).ifPresent(user -> bindingResult.rejectValue("phone", "Account.bind.phoneExist"));

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "手机绑定")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                User user = currentUser().orElseThrow(RuntimeException::new);
                userService.update(user.getId(), u -> u.setPhone(form.getPhone()));
                refreshUser();
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/profile/security"));
    }

    @GetMapping("/security/email")
    public ModelAndView securityEmailPage(final AdminProfileSecurityEmailForm form) {
        return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "邮箱绑定")
                .build();
    }

    @PostMapping("/security/email")
    public ModelAndView securityEmailPage(@Valid final AdminProfileSecurityEmailForm form, BindingResult bindingResult, final AdminProfileSecurityEmailBindForm bindForm) {

        userService.findByEmail(form.getEmail()).ifPresent(user -> bindingResult.rejectValue("email", "Account.bind.emailExist"));

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "邮箱绑定")
                    .build();
        }

        try {
            String code = RandomStringUtils.randomAlphanumeric(8);
            accountService.sendBindEmail(form.getEmail(), code);

            request.getSession().setAttribute(ProfilePageHelper.EMAIL_BINDING_SESSION_ATTR, code);
            request.getSession().setAttribute(ProfilePageHelper.EMAIL_BINDING_CONTENT_SESSION_ATTR, form.getEmail());

            bindForm.setCaptcha("");

            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(bindForm))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "邮箱绑定")
                    .build();
        } catch (Exception e) {
            return page(new Overview(new IconHeader("邮箱绑定失败", new Icon("warning circle"))).addComponent(new P(e.getMessage())).addLink("返回安全设置", QXCMP_BACKEND_URL + "/profile/security")).build();
        }
    }

    @PostMapping("/security/email/bind")
    public ModelAndView securityEmailBindPage(@Valid final AdminProfileSecurityEmailBindForm form, BindingResult bindingResult) {

        if (Objects.isNull(request.getSession().getAttribute(ProfilePageHelper.EMAIL_BINDING_SESSION_ATTR)) || !StringUtils.equals(form.getCaptcha(), (String) request.getSession().getAttribute(ProfilePageHelper.EMAIL_BINDING_SESSION_ATTR)) || bindingResult.hasErrors()) {
            return page(new Overview(new IconHeader("邮箱绑定失败", new Icon("warning circle"))).addComponent(new P("绑定验证不正确或者已过期")).addLink("返回安全设置", QXCMP_BACKEND_URL + "/profile/security")).build();
        }

        return submitForm(form, context -> {
            try {
                User user = currentUser().orElseThrow(RuntimeException::new);
                String email = (String) request.getSession().getAttribute(ProfilePageHelper.EMAIL_BINDING_CONTENT_SESSION_ATTR);
                userService.update(user.getId(), u -> u.setEmail(email));
                refreshUser();
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/profile/security"));
    }

    @GetMapping("/security/question")
    public ModelAndView securityQuestionPage(final AdminProfileSecurityQuestionForm form) {
        return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "密保问题")
                .addObject("selection_items_question1", ProfilePageHelper.QUESTIONS_LIST_1)
                .addObject("selection_items_question2", ProfilePageHelper.QUESTIONS_LIST_2)
                .addObject("selection_items_question3", ProfilePageHelper.QUESTIONS_LIST_3)
                .build();
    }

    @PostMapping("/security/question")
    public ModelAndView securityQuestionPage(@Valid final AdminProfileSecurityQuestionForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return page().addComponent(new TextContainer().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))))
                    .setBreadcrumb("控制台", "", "个人中心", null, "安全设置", "profile/security", "密保问题")
                    .addObject("selection_items_question1", ProfilePageHelper.QUESTIONS_LIST_1)
                    .addObject("selection_items_question2", ProfilePageHelper.QUESTIONS_LIST_2)
                    .addObject("selection_items_question3", ProfilePageHelper.QUESTIONS_LIST_3)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                User user = currentUser().orElseThrow(RuntimeException::new);

                Optional<AccountSecurityQuestion> securityQuestionOptional = securityQuestionService.findByUserId(user.getId());

                if (securityQuestionOptional.isPresent()) {
                    securityQuestionService.update(securityQuestionOptional.get().getId(), securityQuestion -> {
                        securityQuestion.setQuestion1(form.getQuestion1());
                        securityQuestion.setAnswer1(form.getAnswer1());
                        securityQuestion.setQuestion2(form.getQuestion2());
                        securityQuestion.setAnswer2(form.getAnswer2());
                        securityQuestion.setQuestion3(form.getQuestion3());
                        securityQuestion.setAnswer3(form.getAnswer3());
                    });
                } else {
                    securityQuestionService.create(() -> {
                        AccountSecurityQuestion securityQuestion = securityQuestionService.next();
                        securityQuestion.setUserId(user.getId());
                        securityQuestion.setQuestion1(form.getQuestion1());
                        securityQuestion.setAnswer1(form.getAnswer1());
                        securityQuestion.setQuestion2(form.getQuestion2());
                        securityQuestion.setAnswer2(form.getAnswer2());
                        securityQuestion.setQuestion3(form.getQuestion3());
                        securityQuestion.setAnswer3(form.getAnswer3());
                        return securityQuestion;
                    });
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/profile/security"));
    }
}
