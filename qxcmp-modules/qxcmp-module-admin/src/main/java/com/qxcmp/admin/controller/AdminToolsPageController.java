package com.qxcmp.admin.controller;

import com.qxcmp.admin.page.AdminToolsPage;
import com.qxcmp.core.extension.AdminToolPageExtensionPoint;
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
@RequestMapping(ADMIN_URL + "/tools")
@RequiredArgsConstructor
public class AdminToolsPageController extends QxcmpAdminController {

    private final AdminToolPageExtensionPoint adminToolPageExtensionPoint;

    @GetMapping("")
    public ModelAndView toolsPage() {
        return page(AdminToolsPage.class, adminToolPageExtensionPoint.getExtensions());
    }
}
