package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.event.AdminUserRoleEditEvent;
import com.qxcmp.admin.event.AdminUserStatusEditEvent;
import com.qxcmp.admin.form.AdminUserRoleForm;
import com.qxcmp.admin.form.AdminUserStatusForm;
import com.qxcmp.admin.page.AdminUserDetailsPage;
import com.qxcmp.admin.page.AdminUserRoleEditPage;
import com.qxcmp.admin.page.AdminUserStatusEditPage;
import com.qxcmp.admin.page.AdminUserTablePage;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.finance.DepositOrderService;
import com.qxcmp.security.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_USER_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_USER_URL)
@RequiredArgsConstructor
public class AdminUserPageController extends QxcmpAdminController {

    private final RoleService roleService;
    private final DepositOrderService depositOrderService;

    @GetMapping("")
    public ModelAndView tablePage(Pageable pageable) {
        return page(AdminUserTablePage.class, pageable);
    }

    @GetMapping("/{id}/details")
    public ModelAndView userDetailsPage(@PathVariable String id, Pageable pageable) {
        return userService.findOne(id).map(user -> {
                    Page<DepositOrder> depositOrders = depositOrderService.findByUserId(user.getId(), pageable);
            return page(AdminUserDetailsPage.class, user, depositOrders);
                }
        ).orElse(overviewPage(viewHelper.nextWarningOverview("用户不存在").addLink("返回", ADMIN_USER_URL)));
    }

    @GetMapping("/{id}/role")
    public ModelAndView userRoleGet(@PathVariable String id, final AdminUserRoleForm form, BindingResult bindingResult) {
        return entityUpdatePage(AdminUserRoleEditPage.class, id, userService, form, bindingResult)
                .addObject("selection_items_roles", roleService.findAll());
    }

    @PostMapping("/{id}/role")
    public ModelAndView userRolePost(@PathVariable String id, @Valid final AdminUserRoleForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return userRoleGet(id, form, bindingResult);
        }
        ModelAndView modelAndView = updateEntity(id, userService, form);
        applicationContext.publishEvent(new AdminUserRoleEditEvent(currentUser().orElseThrow(RuntimeException::new), userService.findOne(id).orElseThrow(RuntimeException::new)));
        return modelAndView;
    }

    @GetMapping("/{id}/status")
    public ModelAndView userStatusGet(@PathVariable String id, final AdminUserStatusForm form, BindingResult bindingResult) {
        return entityUpdatePage(AdminUserStatusEditPage.class, id, userService, form, bindingResult);
    }

    @PostMapping("/{id}/status")
    public ModelAndView userStatusPost(@PathVariable String id, @Valid final AdminUserStatusForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            userStatusGet(id, form, bindingResult);
        }
        ModelAndView modelAndView = updateEntity(id, userService, form);
        applicationContext.publishEvent(new AdminUserStatusEditEvent(currentUser().orElseThrow(RuntimeException::new), userService.findOne(id).orElseThrow(RuntimeException::new)));
        return modelAndView;
    }
}
