package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.page.AdminAboutPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_URL + "/about")
public class AdminAboutPageController extends QxcmpAdminController {

    @GetMapping("")
    public ModelAndView aboutPage() {
        return page(AdminAboutPage.class);
    }
}
