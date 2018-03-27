package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.form.AdminSmsSettingsForm;
import com.qxcmp.admin.page.AdminSmsOverviewPage;
import com.qxcmp.admin.page.AdminSmsSettingsPage;
import com.qxcmp.core.QxcmpSystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SMS_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_SMS_URL)
@RequiredArgsConstructor
public class AdminSmsPageController extends QxcmpAdminController {

    @GetMapping("")
    public ModelAndView overviewPage() {
        return page(AdminSmsOverviewPage.class);
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
