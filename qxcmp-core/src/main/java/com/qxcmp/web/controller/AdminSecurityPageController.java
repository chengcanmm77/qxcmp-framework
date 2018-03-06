package com.qxcmp.web.controller;

import com.qxcmp.audit.ActionException;
import com.qxcmp.core.event.*;
import com.qxcmp.security.PrivilegeService;
import com.qxcmp.security.Role;
import com.qxcmp.security.RoleService;
import com.qxcmp.web.QxcmpController;
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

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;
import static com.qxcmp.core.QxcmpSystemConfig.*;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/security")
@RequiredArgsConstructor
public class AdminSecurityPageController extends QxcmpController {

    private final PrivilegeService privilegeService;

    private final RoleService roleService;

    @GetMapping("")
    public ModelAndView messagePage() {
        return page().addComponent(new Overview("安全配置")
                .addComponent(convertToTable(stringStringMap -> {
                    stringStringMap.put("角色总数", roleService.count());
                    stringStringMap.put("权限总数", privilegeService.count());
                    stringStringMap.put("验证码阈值", systemConfigService.getInteger(AUTHENTICATION_CAPTCHA_THRESHOLD).orElse(AUTHENTICATION_CAPTCHA_THRESHOLD_DEFAULT));
                    stringStringMap.put("验证码长度", systemConfigService.getInteger(AUTHENTICATION_CAPTCHA_LENGTH).orElse(AUTHENTICATION_CAPTCHA_LENGTH_DEFAULT));
                    stringStringMap.put("是否启用账户锁定", systemConfigService.getBoolean(AUTHENTICATION_ACCOUNT_LOCK).orElse(AUTHENTICATION_ACCOUNT_LOCK_DEFAULT));
                    stringStringMap.put("账户锁定阈值", systemConfigService.getInteger(AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD).orElse(AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD_DEFAULT));
                    stringStringMap.put("账户锁定时长", systemConfigService.getInteger(AUTHENTICATION_ACCOUNT_LOCK_DURATION).orElse(AUTHENTICATION_ACCOUNT_LOCK_DURATION_DEFAULT));
                    stringStringMap.put("是否启用账户过期", systemConfigService.getBoolean(AUTHENTICATION_ACCOUNT_EXPIRE).orElse(AUTHENTICATION_ACCOUNT_EXPIRE_DEFAULT));
                    stringStringMap.put("账户过期时间", systemConfigService.getInteger(AUTHENTICATION_ACCOUNT_EXPIRE_DURATION).orElse(AUTHENTICATION_ACCOUNT_EXPIRE_DURATION_DEFAULT));
                    stringStringMap.put("是否启用密码过期", systemConfigService.getBoolean(AUTHENTICATION_CREDENTIAL_EXPIRE).orElse(AUTHENTICATION_CREDENTIAL_EXPIRE_DEFAULT));
                    stringStringMap.put("密码过期时间", systemConfigService.getInteger(AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION).orElse(AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION_DEFAULT));
                    stringStringMap.put("是否启用唯一密码", systemConfigService.getBoolean(AUTHENTICATION_CREDENTIAL_UNIQUE).orElse(AUTHENTICATION_CREDENTIAL_UNIQUE_DEFAULT));
                })))
                .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, "")
                .build();
    }

    @GetMapping("/role")
    public ModelAndView rolePage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, roleService))
                .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_ROLE)
                .build();
    }

    @GetMapping("/role/new")
    public ModelAndView roleNew(final AdminSecurityRoleNewForm form) {
        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "新建角色")
                .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_ROLE)
                .addObject("selection_items_privileges", privilegeService.findAll())
                .build();
    }

    @PostMapping("/role/new")
    public ModelAndView roleNew(@Valid final AdminSecurityRoleNewForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "新建角色")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_ROLE)
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
        }, (stringObjectMap, overview) -> overview.addLink("返回角色管理", QXCMP_BACKEND_URL + "/security/role").addLink("继续新建角色", QXCMP_BACKEND_URL + "/security/role/new"));
    }

    @GetMapping("/role/{id}/edit")
    public ModelAndView roleEdit(@PathVariable String id, final AdminSecurityRoleEditForm form) {
        return roleService.findOne(id).map(role -> {
            form.setName(role.getName());
            form.setDescription(role.getDescription());
            form.setPrivileges(role.getPrivileges());

            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "编辑角色")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_ROLE)
                    .addObject("selection_items_privileges", privilegeService.findAll())
                    .build();
        }).orElse(page(new Overview(new IconHeader("角色不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/security/role")).build());
    }

    @PostMapping("/role/{id}/edit")
    public ModelAndView roleEdit(@PathVariable String id, @Valid final AdminSecurityRoleEditForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "角色管理", "security/role", "编辑角色")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_ROLE)
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
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/security/role"));
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
                .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_PRIVILEGE)
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
    public ModelAndView authenticationPage(final AdminSecurityAuthenticationForm form) {

        form.setCaptchaThreshold(systemConfigService.getInteger(AUTHENTICATION_CAPTCHA_THRESHOLD).orElse(AUTHENTICATION_CAPTCHA_THRESHOLD_DEFAULT));
        form.setCaptchaLength(systemConfigService.getInteger(AUTHENTICATION_CAPTCHA_LENGTH).orElse(AUTHENTICATION_CAPTCHA_LENGTH_DEFAULT));
        form.setLockAccount(systemConfigService.getBoolean(AUTHENTICATION_ACCOUNT_LOCK).orElse(AUTHENTICATION_ACCOUNT_LOCK_DEFAULT));
        form.setLockThreshold(systemConfigService.getInteger(AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD).orElse(AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD_DEFAULT));
        form.setUnlockDuration(systemConfigService.getInteger(AUTHENTICATION_ACCOUNT_LOCK_DURATION).orElse(AUTHENTICATION_ACCOUNT_LOCK_DURATION_DEFAULT));
        form.setExpireAccount(systemConfigService.getBoolean(AUTHENTICATION_ACCOUNT_EXPIRE).orElse(AUTHENTICATION_ACCOUNT_EXPIRE_DEFAULT));
        form.setExpireAccountDuration(systemConfigService.getInteger(AUTHENTICATION_ACCOUNT_EXPIRE_DURATION).orElse(AUTHENTICATION_ACCOUNT_EXPIRE_DURATION_DEFAULT));
        form.setExpireCredential(systemConfigService.getBoolean(AUTHENTICATION_CREDENTIAL_EXPIRE).orElse(AUTHENTICATION_CREDENTIAL_EXPIRE_DEFAULT));
        form.setExpireCredentialDuration(systemConfigService.getInteger(AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION).orElse(AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION_DEFAULT));
        form.setUniqueCredential(systemConfigService.getBoolean(AUTHENTICATION_CREDENTIAL_UNIQUE).orElse(AUTHENTICATION_CREDENTIAL_UNIQUE_DEFAULT));

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "认证配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_AUTHENTICATION)
                .build();
    }

    @PostMapping("/authentication")
    public ModelAndView authenticationPage(@Valid final AdminSecurityAuthenticationForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统配置", "settings", "安全配置", "security", "认证配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SECURITY, NAVIGATION_ADMIN_SECURITY_AUTHENTICATION)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                systemConfigService.update(AUTHENTICATION_CAPTCHA_THRESHOLD, String.valueOf(form.getCaptchaThreshold()));
                systemConfigService.update(AUTHENTICATION_CAPTCHA_LENGTH, String.valueOf(form.getCaptchaLength()));
                systemConfigService.update(AUTHENTICATION_ACCOUNT_LOCK, String.valueOf(form.isLockAccount()));
                systemConfigService.update(AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD, String.valueOf(form.getLockThreshold()));
                systemConfigService.update(AUTHENTICATION_ACCOUNT_LOCK_DURATION, String.valueOf(form.getUnlockDuration()));
                systemConfigService.update(AUTHENTICATION_ACCOUNT_EXPIRE, String.valueOf(form.isExpireAccount()));
                systemConfigService.update(AUTHENTICATION_ACCOUNT_EXPIRE_DURATION, String.valueOf(form.getExpireAccountDuration()));
                systemConfigService.update(AUTHENTICATION_CREDENTIAL_EXPIRE, String.valueOf(form.isExpireCredential()));
                systemConfigService.update(AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION, String.valueOf(form.getExpireCredentialDuration()));
                systemConfigService.update(AUTHENTICATION_CREDENTIAL_UNIQUE, String.valueOf(form.isUniqueCredential()));

                applicationContext.publishEvent(new AdminSecurityAuthenticationEvent(currentUser().orElseThrow(RuntimeException::new)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }
}
