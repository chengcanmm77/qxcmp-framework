package com.qxcmp.admin.controller;

import com.google.common.collect.Maps;
import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.form.AdminSmsSendForm;
import com.qxcmp.admin.form.AdminSmsSettingsForm;
import com.qxcmp.admin.page.AdminSmsOverviewPage;
import com.qxcmp.admin.page.AdminSmsSendPage;
import com.qxcmp.admin.page.AdminSmsSendRecordDetailsPage;
import com.qxcmp.admin.page.AdminSmsSettingsPage;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.message.SmsSendRecordService;
import com.qxcmp.message.SmsService;
import com.qxcmp.message.SmsTemplateExtension;
import com.qxcmp.message.SmsTemplateExtensionPoint;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SMS_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_SMS_URL)
@RequiredArgsConstructor
public class AdminSmsPageController extends QxcmpAdminController {

    private final SmsService smsService;
    private final SmsSendRecordService smsSendRecordService;
    private final SmsTemplateExtensionPoint smsTemplateExtensionPoint;

    @GetMapping("")
    public ModelAndView overviewPage(Pageable pageable) {
        return page(AdminSmsOverviewPage.class, pageable);
    }

    @GetMapping("/{id}/details")
    public ModelAndView detailsPage(@PathVariable String id) {
        return entityDetailsPage(AdminSmsSendRecordDetailsPage.class, id, smsSendRecordService);
    }

    @GetMapping("/send")
    public ModelAndView sendGet(final AdminSmsSendForm form, BindingResult bindingResult) {
        return page(AdminSmsSendPage.class, form, bindingResult)
                .addObject("selection_items_name", smsTemplateExtensionPoint.getExtensions().stream().map(SmsTemplateExtension::getName).collect(Collectors.toList()));
    }

    @PostMapping("/send")
    public ModelAndView sendPost(@Valid final AdminSmsSendForm form, BindingResult bindingResult) {
        LinkedHashMap<Object, Object> parameters = Maps.newLinkedHashMap();
        try {
            Arrays.stream(StringUtils.split(form.getParameter(), "\n"))
                    .forEach(s -> {
                        String key = s.split(":")[0].trim();
                        String value = s.split(":")[1].trim();
                        parameters.put(key, value);
                    });
        } catch (Exception e) {
            bindingResult.rejectValue("parameter", "", "参数格式不正确");
        }

        if (bindingResult.hasErrors()) {
            return sendGet(form, bindingResult);
        }
        return execute("发送短信", context -> {
            try {
                Set<String> phones = Arrays.stream(form.getPhones().split("\n")).map(String::trim).collect(Collectors.toSet());
                smsService.send(phones, smsTemplateExtensionPoint.getExtensions().stream().filter(extension -> StringUtils.equals(extension.getName(), form.getName())).map(SmsTemplateExtension::getClass).findAny().orElseThrow(RuntimeException::new), parameters);
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("继续发送", "").addLink("返回", ADMIN_SMS_URL));
    }

    @GetMapping("/settings")
    public ModelAndView settingsGet(final AdminSmsSettingsForm form, BindingResult bindingResult) {
        return systemConfigPage(AdminSmsSettingsPage.class, form, bindingResult, QxcmpSystemConfig.class);
    }

    @PostMapping("/settings")
    public ModelAndView settingsPost(@Valid final AdminSmsSettingsForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return settingsGet(form, bindingResult);
        }
        return updateSystemConfig(QxcmpSystemConfig.class, form);
    }
}
