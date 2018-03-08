package com.qxcmp.admin;

import com.qxcmp.admin.page.AdminPageRevolver;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.views.Overview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 后台页面路由基类
 *
 * @author Aaric
 */
public abstract class QxcmpAdminController extends QxcmpController {

    private AdminPageRevolver adminPageRevolver;

    /**
     * 获取后台错误页面
     *
     * @param errors 错误信息
     * @return 后台错误页面
     */
    protected ModelAndView adminErrorPage(Map<String, Object> errors) {
        return page(adminPageRevolver.getAdminErrorPage(), errors);
    }

    @Override
    protected ModelAndView overviewPage(Overview overview) {
        return page(adminPageRevolver.getAdminOverviewPage(), overview);
    }

    @Autowired
    public void setAdminPageRevolver(AdminPageRevolver adminPageRevolver) {
        this.adminPageRevolver = adminPageRevolver;
    }
}
