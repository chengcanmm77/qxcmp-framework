package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.event.AdminSecurityPrivilegeDisableEvent;
import com.qxcmp.admin.event.AdminSecurityPrivilegeEnableEvent;
import com.qxcmp.admin.form.AdminSecurityAuthenticationForm;
import com.qxcmp.admin.form.AdminSecurityRoleEditForm;
import com.qxcmp.admin.form.AdminSecurityRoleNewForm;
import com.qxcmp.admin.page.*;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.security.PrivilegeService;
import com.qxcmp.security.RoleService;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SECURITY_URL;

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
        return page(AdminSecurityRoleTablePage.class, roleService, pageable);
    }

    @GetMapping("/role/new")
    public ModelAndView roleNewGet(final AdminSecurityRoleNewForm form, BindingResult bindingResult) {
        return page(AdminSecurityRoleNewPage.class, form, bindingResult)
                .addObject("selection_items_privileges", privilegeService.findAll());
    }

    @PostMapping("/role/new")
    public ModelAndView roleNewPost(@Valid final AdminSecurityRoleNewForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return roleNewGet(form, bindingResult);
        }
        return createEntity(roleService, form);
    }

    @GetMapping("/role/{id}/edit")
    public ModelAndView roleEditGet(@PathVariable Long id, final AdminSecurityRoleEditForm form, BindingResult bindingResult) {
        return entityUpdatePage(AdminSecurityRoleEditPage.class, id, roleService, form, bindingResult)
                .addObject("selection_items_privileges", privilegeService.findAll());
    }

    @PostMapping("/role/{id}/edit")
    public ModelAndView roleEditPost(@PathVariable Long id, @Valid final AdminSecurityRoleEditForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            roleEditGet(id, form, bindingResult);
        }
        return updateEntity(id, roleService, form);
    }

    @PostMapping("/role/{id}/remove")
    public ResponseEntity<RestfulResponse> roleRemove(@PathVariable String id) {
        return deleteEntity("删除角色", Long.parseLong(id), roleService);
    }

    @PostMapping("/role/remove")
    public ResponseEntity<RestfulResponse> roleBatchRemove(@RequestParam("keys[]") List<String> keys) {
        for (String key : keys) {
            deleteEntity("删除角色", Long.parseLong(key), roleService);
        }
        return ResponseEntity.ok().body(RestfulResponse.builder().status(HttpStatus.OK.value()).build());
    }

    @GetMapping("/privilege")
    public ModelAndView privilegePage(Pageable pageable) {
        return page(AdminSecurityPrivilegeTablePage.class, privilegeService, pageable);
    }

    @PostMapping("/privilege/{id}/enable")
    public ResponseEntity<RestfulResponse> privilegeEnable(@PathVariable Long id) {
        return execute("启用权限", context -> {
            try {
                applicationContext.publishEvent(new AdminSecurityPrivilegeEnableEvent(currentUser().orElseThrow(RuntimeException::new), privilegeService.update(id, privilege -> privilege.setDisabled(false))));

            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @PostMapping("/privilege/{id}/disable")
    public ResponseEntity<RestfulResponse> privilegeDisable(@PathVariable Long id) {
        return execute("启用权限", context -> {
            try {
                applicationContext.publishEvent(new AdminSecurityPrivilegeDisableEvent(currentUser().orElseThrow(RuntimeException::new), privilegeService.update(id, privilege -> privilege.setDisabled(true))));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
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
