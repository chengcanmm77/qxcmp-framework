package com.qxcmp.news.web;

import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.views.Overview;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.NAVIGATION_ADMIN_NEWS;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/news")
public class AdminNewsPageController extends QxcmpController {

    @GetMapping("")
    public ModelAndView newsPage() {
        return page().addComponent(new Overview("新闻管理"))
                .setBreadcrumb("控制台", "", "新闻管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, "")
                .build();
    }

}
