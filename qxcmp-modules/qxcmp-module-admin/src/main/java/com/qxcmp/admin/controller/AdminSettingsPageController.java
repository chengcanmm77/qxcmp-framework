package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.page.AdminSettingsPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_URL + "/settings")
@RequiredArgsConstructor
public class AdminSettingsPageController extends QxcmpAdminController {

    @GetMapping("")
    public ModelAndView settingsPage() {
        return page(AdminSettingsPage.class);
    }
}
