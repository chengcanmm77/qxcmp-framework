package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.QxcmpAdminModuleNavigation;
import com.qxcmp.admin.event.AdminSecurityPrivilegeDisableEvent;
import com.qxcmp.admin.event.AdminSecurityPrivilegeEnableEvent;
import com.qxcmp.admin.event.AdminSecurityRoleEditEvent;
import com.qxcmp.admin.event.AdminSecurityRoleNewEvent;
import com.qxcmp.admin.page.AdminSecurityAuthenticationPage;
import com.qxcmp.admin.page.AdminSecurityPage;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.security.PrivilegeService;
import com.qxcmp.security.Role;
import com.qxcmp.security.RoleService;
import com.qxcmp.web.form.AdminSecurityAuthenticationForm;
import com.qxcmp.web.form.AdminSecurityRoleEditForm;
import com.qxcmp.web.form.AdminSecurityRoleNewForm;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SECURITY_URL;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_SECURITY_URL)
@RequiredArgsConstructor
public class AdminSecurityPageController extends QxcmpAdminController {

    private final PrivilegeService privilegeService;
    private final RoleService roleService;

    @GetMapping("")
    public ModelAndView messagePage() {
        return page(AdminSecurityPage.class);
    }

    @GetMapping("/role")
    public ModelAndView rolePage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, roleService))
                .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理")
                .setVerticalNavigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY, QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_ROLE)
                .build();
    }

    @GetMapping("/role/new")
    public ModelAndView roleNew(final AdminSecurityRoleNewForm form) {
        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "新建角色")
                .setVerticalNavigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY, QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_ROLE)
                .addObject("selection_items_privileges", privilegeService.findAll())
                .build();
    }

    @PostMapping("/role/new")
    public ModelAndView roleNew(@Valid final AdminSecurityRoleNewForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "新建角色")
                    .setVerticalNavigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY, QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_ROLE)
                    .addObject("selection_items_privileges", privilegeService.findAll())
                    .build();
        }

        return submitForm(form, context -> {
            try {
                applicationContext.publishEvent(new AdminSecurityRoleNewEvent(currentUser().orElseThrow(RuntimeException::new), roleService.create(() -> {
                    Role role = roleService.next();
                    role.setName(form.getName());
                    role.setDescription(form.getDescription());
                    role.setPrivileges(form.getPrivileges());
                    return role;
                })));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回角色管理", QXCMP_ADMIN_URL + "/security/role").addLink("继续新建角色", QXCMP_ADMIN_URL + "/security/role/new"));
    }

    @GetMapping("/role/{id}/edit")
    public ModelAndView roleEdit(@PathVariable String id, final AdminSecurityRoleEditForm form) {
        return roleService.findOne(id).map(role -> {
            form.setName(role.getName());
            form.setDescription(role.getDescription());
            form.setPrivileges(role.getPrivileges());

            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "编辑角色")
                    .setVerticalNavigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY, QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_ROLE)
                    .addObject("selection_items_privileges", privilegeService.findAll())
                    .build();
        }).orElse(page(new Overview(new IconHeader("角色不存在", new Icon("warning circle"))).addLink("返回", QXCMP_ADMIN_URL + "/security/role")).build());
    }

    @PostMapping("/role/{id}/edit")
    public ModelAndView roleEdit(@PathVariable String id, @Valid final AdminSecurityRoleEditForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "编辑角色")
                    .setVerticalNavigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY, QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_ROLE)
                    .addObject("selection_items_privileges", privilegeService.findAll())
                    .build();
        }

        return submitForm(form, context -> {
            try {
                applicationContext.publishEvent(new AdminSecurityRoleEditEvent(currentUser().orElseThrow(RuntimeException::new), roleService.update(Long.parseLong(id), role -> {
                    role.setName(form.getName());
                    role.setDescription(form.getDescription());
                    role.setPrivileges(form.getPrivileges());
                })));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_ADMIN_URL + "/security/role"));
    }

    @PostMapping("/role/{id}/remove")
    public ResponseEntity<RestfulResponse> roleRemove(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("删除角色", context -> {
            try {
                roleService.deleteById(Long.parseLong(id));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @PostMapping("/role/remove")
    public ResponseEntity<RestfulResponse> roleBatchRemove(@RequestParam("keys[]") List<String> keys) {
        RestfulResponse restfulResponse = audit("批量删除角色", context -> {
            try {
                for (String key : keys) {
                    roleService.deleteById(Long.parseLong(key));
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @GetMapping("/privilege")
    public ModelAndView privilegePage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, privilegeService))
                .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "权限管理")
                .setVerticalNavigation(QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY, QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_PRIVILEGE)
                .build();
    }

    @PostMapping("/privilege/{id}/enable")
    public ResponseEntity<RestfulResponse> privilegeEnable(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("启用权限", context -> {
            try {
                applicationContext.publishEvent(new AdminSecurityPrivilegeEnableEvent(currentUser().orElseThrow(RuntimeException::new), privilegeService.update(Long.parseLong(id), privilege -> privilege.setDisabled(false))));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @PostMapping("/privilege/{id}/disable")
    public ResponseEntity<RestfulResponse> privilegeDisable(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("禁用权限", context -> {
            try {

                applicationContext.publishEvent(new AdminSecurityPrivilegeDisableEvent(currentUser().orElseThrow(RuntimeException::new), privilegeService.update(Long.parseLong(id), privilege -> privilege.setDisabled(true))));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @GetMapping("/authentication")
    public ModelAndView authenticationGet(final AdminSecurityAuthenticationForm form, BindingResult bindingResult) {
        return systemConfigPage(AdminSecurityAuthenticationPage.class, form, bindingResult, QxcmpSystemConfig.class);
    }

    @PostMapping("/authentication")
    public ModelAndView authenticationPost(@Valid final AdminSecurityAuthenticationForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            authenticationGet(form, bindingResult);
        }
        return updateSystemConfig(QxcmpSystemConfig.class, form);
    }
}
