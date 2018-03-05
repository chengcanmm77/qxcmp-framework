package com.qxcmp.web.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.qxcmp.audit.ActionException;
import com.qxcmp.message.*;
import com.qxcmp.security.RoleService;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.*;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;
import static com.qxcmp.core.QxcmpSystemConfigConfiguration.*;

/**
 * 消息服务后台页面
 *
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/message")
@RequiredArgsConstructor
public class AdminMessageController extends QxcmpController {

    private static final List<String> SITE_NOTIFICATION_TYPE = ImmutableList.of("一般消息", "网站通知", "网站警告", "网站错误");

    private final EmailService emailService;
    private final SmsService smsService;
    private final SmsTemplateService smsTemplateService;
    private final SiteNotificationService siteNotificationService;
    private final InnerMessageService innerMessageService;
    private final RoleService roleService;

    @GetMapping("")
    public ModelAndView messagePage() {
        return page().addComponent(new Overview("消息服务")
                .addComponent(convertToTable(stringStringMap -> {
                    stringStringMap.put("邮件服务 - 主机名", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME_DEFAULT_VALUE));
                    stringStringMap.put("邮件服务 - 端口", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT).orElse(String.valueOf(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT_DEFAULT_VALUE)));
                    stringStringMap.put("邮件服务 - 用户名", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME_DEFAULT_VALUE));
                    stringStringMap.put("邮件服务 - 密码", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD_DEFAULT_VALUE));
                    stringStringMap.put("短信服务 - AccessKey", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY).orElse(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY_DEFAULT_VALUE));
                    stringStringMap.put("短信服务 - AccessSecret", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET).orElse(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET_DEFAULT_VALUE));
                    stringStringMap.put("短信服务 - EndPoint", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT).orElse(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT_DEFAULT_VALUE));
                    stringStringMap.put("短信服务 - 主题名称", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF).orElse(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF_DEFAULT_VALUE));
                    stringStringMap.put("短信服务 - 短信签名ID", systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_SIGN).orElse(SYSTEM_CONFIG_MESSAGE_SMS_SIGN_DEFAULT_VALUE));
                })))
                .setBreadcrumb("控制台", "", "消息服务")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, "")
                .build();
    }

    @GetMapping("/sms/send")
    public ModelAndView smsSendPage(final AdminMessageSmsSendForm form) {
        SmsMessageParameter smsMessageParameter = new SmsMessageParameter();
        smsMessageParameter.setKey("captcha");
        smsMessageParameter.setValue(RandomStringUtils.randomNumeric(6));

        form.setTemplateCode(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE).orElse(""));
        form.getParameters().add(smsMessageParameter);
        form.getPhones().add(currentUser().orElseThrow(null).getPhone());

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "消息服务", "message", "短信发送服务")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SEND)
                .build();
    }

    @PostMapping("/sms/send")
    public ModelAndView smsSendPage(@Valid final AdminMessageSmsSendForm form, BindingResult bindingResult,
                                    @RequestParam(value = "add_phones", required = false) boolean addPhones,
                                    @RequestParam(value = "remove_phones", required = false) Integer removePhones,
                                    @RequestParam(value = "add_parameters", required = false) boolean addParameters,
                                    @RequestParam(value = "remove_parameters", required = false) Integer removeParameters) {

        if (addPhones) {
            form.getPhones().add("");
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SEND)
                    .build();
        }

        if (Objects.nonNull(removePhones)) {
            form.getPhones().remove(removePhones.intValue());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SEND)
                    .build();
        }

        if (addParameters) {
            form.getParameters().add(new SmsMessageParameter());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SEND)
                    .build();
        }

        if (Objects.nonNull(removeParameters)) {
            form.getParameters().remove(removeParameters.intValue());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SEND)
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SEND)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                smsService.send(form.getPhones(), form.getTemplateCode(), stringStringMap -> {
                    form.getParameters().forEach(smsMessageParameter -> stringStringMap.put(smsMessageParameter.getKey(), smsMessageParameter.getValue()));
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @GetMapping("/email/send")
    public ModelAndView emailSendPage(final AdminMessageEmailSendForm form) {
        form.getTo().add("");
        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "消息服务", "message", "邮件发送服务")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND)
                .build();
    }

    @PostMapping("/email/send")
    public ModelAndView emailSendPage(@Valid final AdminMessageEmailSendForm form, BindingResult bindingResult,
                                      @RequestParam(value = "add_to", required = false) boolean addTo,
                                      @RequestParam(value = "remove_to", required = false) Integer removeTo,
                                      @RequestParam(value = "add_cc", required = false) boolean addCc,
                                      @RequestParam(value = "remove_cc", required = false) Integer removeCc) {

        if (addTo) {
            form.getTo().add("");
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "邮件发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND)
                    .build();
        }

        if (Objects.nonNull(removeTo)) {
            form.getTo().remove(removeTo.intValue());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "邮件发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND)
                    .build();
        }

        if (addCc) {
            form.getCc().add("");
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "邮件发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND)
                    .build();
        }

        if (Objects.nonNull(removeCc)) {
            form.getCc().remove(removeCc.intValue());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "邮件发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND)
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "邮件发送服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                emailService.send(mimeMessageHelper -> {
                    mimeMessageHelper.setSubject(form.getSubject());
                    form.getTo().forEach(s -> {
                        try {
                            mimeMessageHelper.addTo(s);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    });

                    form.getCc().forEach(s -> {
                        try {
                            mimeMessageHelper.addCc(s);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    });

                    mimeMessageHelper.setText(form.getContent());
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @GetMapping("/email/settings")
    public ModelAndView emailSettingsPage(final AdminMessageEmailSettingsForm form) {

        form.setHost(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME_DEFAULT_VALUE));
        form.setPort(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT).orElse(String.valueOf(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT_DEFAULT_VALUE)));
        form.setUsername(systemConfigService.getString(SYSTEM_CONFIG_ACCOUNT_ENABLE_USERNAME).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME_DEFAULT_VALUE));
        form.setPassword(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD_DEFAULT_VALUE));
        form.setActivateSubject(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT_DEFAULT_VALUE));
        form.setActivateContent(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT_DEFAULT_VALUE));
        form.setResetSubject(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT_DEFAULT_VALUE));
        form.setResetContent(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT_DEFAULT_VALUE));
        form.setBindingSubject(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT_DEFAULT_VALUE));
        form.setBindingContent(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT_DEFAULT_VALUE));

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "消息服务", "message", "邮件服务配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SETTINGS)
                .build();
    }

    @PostMapping("/email/settings")
    public ModelAndView emailSettingsPage(@Valid final AdminMessageEmailSettingsForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "邮件服务配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_EMAIL_SETTINGS)
                    .build();
        }

        return submitForm(form, context -> {
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME, form.getHost());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT, form.getPort());
            systemConfigService.update(SYSTEM_CONFIG_ACCOUNT_ENABLE_USERNAME, form.getUsername());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD, form.getPassword());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT, form.getActivateSubject());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT, form.getActivateContent());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT, form.getResetSubject());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT, form.getResetContent());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT, form.getBindingSubject());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT, form.getBindingContent());

            emailService.config();
        });
    }


    @GetMapping("/sms/settings")
    public ModelAndView smsSettingsPage(final AdminMessageSmsSettingsForm form) {

        form.setAccessKey(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY).orElse(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY_DEFAULT_VALUE));
        form.setAccessSecret(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET).orElse(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET_DEFAULT_VALUE));
        form.setEndPoint(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT).orElse(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT_DEFAULT_VALUE));
        form.setTopicRef(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF).orElse(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF_DEFAULT_VALUE));
        form.setSign(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_SIGN).orElse(SYSTEM_CONFIG_MESSAGE_SMS_SIGN_DEFAULT_VALUE));
        form.setCaptchaTemplate(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE).orElse(SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE_DEFAULT_VALUE));
        form.setTemplates(smsTemplateService.findAll());

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "消息服务", "message", "短信服务配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SETTINGS)
                .build();
    }

    @PostMapping("/sms/settings")
    public ModelAndView smsSettingsPage(@Valid final AdminMessageSmsSettingsForm form, BindingResult bindingResult,
                                        @RequestParam(value = "add_templates", required = false) boolean addTemplates,
                                        @RequestParam(value = "remove_templates", required = false) Integer removeTemplates) {

        if (addTemplates) {
            form.getTemplates().add(smsTemplateService.next());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信服务配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SETTINGS)
                    .build();
        }

        if (Objects.nonNull(removeTemplates)) {
            try {
                smsTemplateService.delete(form.getTemplates().remove(removeTemplates.intValue()));
            } catch (Exception ignored) {

            }
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信服务配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SETTINGS)
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "短信服务配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SMS_SETTINGS)
                    .build();
        }

        return submitForm(form, context -> {
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY, form.getAccessKey());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET, form.getAccessSecret());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT, form.getEndPoint());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF, form.getTopicRef());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_SIGN, form.getSign());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE, form.getCaptchaTemplate());

            form.getTemplates().stream()
                    .filter(smsTemplate -> StringUtils.isNotBlank(smsTemplate.getId()) && StringUtils.isNotBlank(smsTemplate.getTitle()) && StringUtils.isNotBlank(smsTemplate.getTemplate()))
                    .forEach(smsTemplateService::save);

            smsService.config();
        });
    }

    @GetMapping("/site/notification")
    public ModelAndView siteNotificationPage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, siteNotificationService))
                .setBreadcrumb("控制台", "", "消息服务", "message", "网站通知服务")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SITE_NOTIFICATION)
                .build();
    }

    @GetMapping("/site/notification/new")
    public ModelAndView siteNotificationNewPage(final AdminMessageSiteNotificationNewForm form) {

        form.setType(SITE_NOTIFICATION_TYPE.get(0));
        form.setDateStart(new Date());
        form.setDateEnd(DateTime.now().plusDays(1).toDate());

        return page().addComponent(convertToForm(form))
                .setBreadcrumb("控制台", "", "消息服务", "message", "网站通知服务", "message/site/notification", "新建网站通知")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SITE_NOTIFICATION)
                .addObject("selection_items_type", SITE_NOTIFICATION_TYPE)
                .build();
    }

    @PostMapping("/site/notification/new")
    public ModelAndView siteNotificationNewPage(@Valid final AdminMessageSiteNotificationNewForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "网站通知服务", "message/site/notification", "新建网站通知")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SITE_NOTIFICATION)
                    .addObject("selection_items_type", SITE_NOTIFICATION_TYPE)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                siteNotificationService.create(() -> {
                    SiteNotification siteNotification = siteNotificationService.next();
                    siteNotification.setOwnerId(currentUser().map(User::getId).orElse(""));
                    siteNotification.setType(form.getType());
                    siteNotification.setDateStart(form.getDateStart());
                    siteNotification.setDateEnd(form.getDateEnd());
                    siteNotification.setTitle(form.getTitle());
                    siteNotification.setContent(form.getContent());
                    return siteNotification;
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/message/site/notification").addLink("继续新建", QXCMP_BACKEND_URL + "/message/site/notification/new"));
    }

    @GetMapping("/site/notification/{id}/edit")
    public ModelAndView siteNotificationEditPage(@PathVariable String id, final AdminMessageSiteNotificationEditForm form) {
        return siteNotificationService.findOne(id)
                .map(siteNotification -> {

                    form.setType(siteNotification.getType());
                    form.setDateStart(siteNotification.getDateStart());
                    form.setDateEnd(siteNotification.getDateEnd());
                    form.setTitle(siteNotification.getTitle());
                    form.setContent(siteNotification.getContent());

                    return page().addComponent(convertToForm(form))
                            .setBreadcrumb("控制台", "", "消息服务", "message", "网站通知服务", "message/site/notification", "编辑网站通知")
                            .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SITE_NOTIFICATION)
                            .addObject("selection_items_type", SITE_NOTIFICATION_TYPE)
                            .build();
                })
                .orElse(page(viewHelper.nextWarningOverview("网站通知不存在", "").addLink("返回", QXCMP_BACKEND_URL + "/message/site/notification")).build());
    }

    @PostMapping("/site/notification/{id}/edit")
    public ModelAndView siteNotificationEditPage(@PathVariable String id, @Valid final AdminMessageSiteNotificationEditForm form, BindingResult bindingResult) {
        return siteNotificationService.findOne(id)
                .map(siteNotification -> {

                    if (bindingResult.hasErrors()) {
                        return page().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                                .setBreadcrumb("控制台", "", "消息服务", "message", "网站通知服务", "message/site/notification", "编辑网站通知")
                                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_SITE_NOTIFICATION)
                                .addObject("selection_items_type", SITE_NOTIFICATION_TYPE)
                                .build();
                    }

                    return submitForm(form, context -> {
                        try {
                            siteNotificationService.update(siteNotification.getId(), notification -> {
                                notification.setOwnerId(currentUser().map(User::getId).orElse(""));
                                notification.setType(form.getType());
                                notification.setDateStart(form.getDateStart());
                                notification.setDateEnd(form.getDateEnd());
                                notification.setTitle(form.getTitle());
                                notification.setContent(form.getContent());
                            });
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/message/site/notification"));
                })
                .orElse(page(viewHelper.nextWarningOverview("网站通知不存在", "").addLink("返回", QXCMP_BACKEND_URL + "/message/site/notification")).build());
    }


    @PostMapping("/site/notification/{id}/remove")
    public ResponseEntity<RestfulResponse> siteNotificationRemove(@PathVariable String id) {
        return siteNotificationService.findOne(id)
                .map(siteNotification -> {
                    RestfulResponse restfulResponse = audit("删除网站通知", context -> {
                        try {
                            siteNotificationService.delete(siteNotification);
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @GetMapping("/inner/message")
    public ModelAndView innerMessagePage(final AdminMessageInnerMessageForm form) {
        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "消息服务", "message", "站内信服务")
                .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_INNER_MESSAGE)
                .addObject("selection_items_group", roleService.findAll())
                .build();
    }

    @PostMapping("/inner/message")
    public ModelAndView innerMessagePage(@Valid final AdminMessageInnerMessageForm form, BindingResult bindingResult,
                                         @RequestParam(value = "add_receivers", required = false) boolean addReceivers,
                                         @RequestParam(value = "remove_receivers", required = false) Integer removeReceivers) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        if (addReceivers) {
            form.getReceivers().add("");
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "站内信服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_INNER_MESSAGE)
                    .addObject("selection_items_group", roleService.findAll())
                    .build();
        }

        if (Objects.nonNull(removeReceivers)) {
            form.getReceivers().remove(removeReceivers.intValue());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "站内信服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_INNER_MESSAGE)
                    .addObject("selection_items_group", roleService.findAll())
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "消息服务", "message", "站内信服务")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MESSAGE, NAVIGATION_ADMIN_MESSAGE_INNER_MESSAGE)
                    .addObject("selection_items_group", roleService.findAll())
                    .build();
        }

        return submitForm(form, context -> {
            try {

                int totalCount;
                AtomicInteger successCount = new AtomicInteger();

                Set<String> targets = Sets.newHashSet();

                if (form.isSendToAll()) {
                    targets.addAll(userService.findAll().stream().map(User::getId).collect(Collectors.toList()));
                } else {
                    targets.addAll(form.getReceivers());
                    form.getGroup().forEach(role -> targets.addAll(userService.findByRole(role).stream().map(User::getId).collect(Collectors.toList())));
                }

                totalCount = targets.size();

                targets.stream().filter(s -> userService.findOne(s).isPresent()).forEach(s -> {
                    try {
                        innerMessageService.create(() -> {
                                    InnerMessage message = innerMessageService.next();
                                    message.setSender(user.getId());
                                    message.setTitle(form.getTitle());
                                    message.setContent(form.getContent());
                                    message.setUserId(s);
                                    message.setUnread(true);
                                    message.setSendTime(new Date());
                                    return message;
                                }
                        );

                        successCount.incrementAndGet();
                    } catch (Exception ignored) {

                    }
                });

                context.put("totalCount", totalCount);
                context.put("successCount", successCount.intValue());

            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview
                .addComponent(convertToTable(objectObjectMap -> {
                            objectObjectMap.put("总共发送条数", stringObjectMap.get("totalCount"));
                            objectObjectMap.put("成功发送条数", stringObjectMap.get("successCount"));
                        }
                ))
                .addLink("返回消息服务", QXCMP_BACKEND_URL + "/message").addLink("继续发送站内信", ""));
    }
}
