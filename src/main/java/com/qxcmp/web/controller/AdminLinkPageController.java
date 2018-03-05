package com.qxcmp.web.controller;

import com.google.common.collect.ImmutableList;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.event.AdminLinkEditEvent;
import com.qxcmp.core.event.AdminLinkNewEvent;
import com.qxcmp.core.event.AdminLinkSettingsEvent;
import com.qxcmp.link.Link;
import com.qxcmp.link.LinkService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminLinkEditForm;
import com.qxcmp.web.form.AdminLinkNewForm;
import com.qxcmp.web.form.AdminLinkSettingsForm;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;
import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_LINK_TYPE;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/link")
@RequiredArgsConstructor
public class AdminLinkPageController extends QxcmpController {

    private static final List<String> SUPPORT_TARGET = ImmutableList.of("当前窗口打开", "新窗口打开");

    private final LinkService linkService;

    @GetMapping("")
    public ModelAndView linkPage(Pageable pageable) {
        return page().addComponent(convertToTable(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.ASC, "type"), new Sort.Order(Sort.Direction.DESC, "dateModified"))), linkService))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_ALL)
                .build();
    }

    @GetMapping("/new")
    public ModelAndView linkNewPage(final AdminLinkNewForm form) {
        List<String> types = systemConfigService.getList(SYSTEM_CONFIG_LINK_TYPE);

        if (!types.isEmpty()) {
            form.setType(types.get(0));
        }

        form.setTarget(SUPPORT_TARGET.get(0));

        return page().addComponent(convertToForm(form))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "添加链接")
                .addObject("selection_items_type", types)
                .addObject("selection_items_target", SUPPORT_TARGET)
                .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_ALL)
                .build();
    }

    @PostMapping("/new")
    public ModelAndView linkNewPage(@Valid final AdminLinkNewForm form, BindingResult bindingResult) {
        List<String> types = systemConfigService.getList(SYSTEM_CONFIG_LINK_TYPE);

        if (bindingResult.hasErrors()) {
            return page().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "添加链接")
                    .addObject("selection_items_type", types)
                    .addObject("selection_items_target", SUPPORT_TARGET)
                    .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_ALL)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                Link link = linkService.next();
                link.setTitle(form.getTitle());
                link.setDateCreated(new Date());
                link.setDateModified(new Date());
                link.setHref(form.getHref());
                link.setType(form.getType());
                link.setSort(form.getSort());

                if (form.getTarget().equals(SUPPORT_TARGET.get(0))) {
                    link.setTarget(AnchorTarget.NONE.toString());
                } else {
                    link.setTarget(AnchorTarget.BLANK.toString());
                }

                applicationContext.publishEvent(new AdminLinkNewEvent(currentUser().orElseThrow(RuntimeException::new), linkService.create(() -> link)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/link").addLink("继续添加链接", ""));
    }

    @GetMapping("/{id}/edit")
    public ModelAndView linkEditPage(@PathVariable String id, final AdminLinkEditForm form) {
        return linkService.findOne(id).map(link -> {

            form.setTitle(link.getTitle());
            form.setType(link.getType());
            form.setHref(link.getHref());

            if (StringUtils.isBlank(link.getTarget())) {
                form.setTarget(SUPPORT_TARGET.get(0));
            } else {
                form.setTarget(SUPPORT_TARGET.get(1));
            }

            form.setSort(link.getSort());

            return page().addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "编辑链接")
                    .addObject("selection_items_type", systemConfigService.getList(SYSTEM_CONFIG_LINK_TYPE))
                    .addObject("selection_items_target", SUPPORT_TARGET)
                    .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_ALL)
                    .build();
        }).orElse(page(viewHelper.nextWarningOverview("链接不存在", "").addLink("返回", QXCMP_BACKEND_URL + "/link")).build());
    }

    @PostMapping("/{id}/edit")
    public ModelAndView linkEditPage(@PathVariable String id, @Valid final AdminLinkEditForm form, BindingResult bindingResult) {
        return linkService.findOne(id).map(link -> {

            if (bindingResult.hasErrors()) {
                return page().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                        .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "编辑链接")
                        .addObject("selection_items_type", systemConfigService.getList(SYSTEM_CONFIG_LINK_TYPE))
                        .addObject("selection_items_target", SUPPORT_TARGET)
                        .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_ALL)
                        .build();
            }

            return submitForm(form, context -> {
                try {
                    applicationContext.publishEvent(new AdminLinkEditEvent(currentUser().orElseThrow(RuntimeException::new), linkService.update(link.getId(), target -> {
                        target.setTitle(form.getTitle());
                        target.setDateModified(new Date());
                        target.setHref(form.getHref());
                        target.setType(form.getType());
                        target.setSort(form.getSort());

                        if (form.getTarget().equals(SUPPORT_TARGET.get(0))) {
                            target.setTarget(AnchorTarget.NONE.toString());
                        } else {
                            target.setTarget(AnchorTarget.BLANK.toString());
                        }
                    })));
                } catch (Exception e) {
                    throw new ActionException(e.getMessage(), e);
                }
            }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/link"));
        }).orElse(page(viewHelper.nextWarningOverview("链接不存在", "").addLink("返回", QXCMP_BACKEND_URL + "/link")).build());
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> linkRemove(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("删除链接", context -> {
            try {
                linkService.deleteById(Long.parseLong(id));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @GetMapping("/settings")
    public ModelAndView linkSettingPage(final AdminLinkSettingsForm form) {
        form.setType(systemConfigService.getList(SYSTEM_CONFIG_LINK_TYPE));
        return page().addComponent(convertToForm(form))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "链接设置")
                .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_SETTINGS)
                .build();
    }

    @PostMapping("/settings")
    public ModelAndView linkSettingPage(@Valid final AdminLinkSettingsForm form, BindingResult bindingResult,
                                        @RequestParam(value = "add_type", required = false) boolean addType,
                                        @RequestParam(value = "remove_type", required = false) Integer removeType) {

        if (addType) {
            form.getType().add("");
            return page().addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "链接设置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_SETTINGS)
                    .build();
        }

        if (Objects.nonNull(removeType)) {
            form.getType().remove(removeType.intValue());
            return page().addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "链接设置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_SETTINGS)
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "链接管理", "new", "链接设置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_LINK, NAVIGATION_ADMIN_LINK_SETTINGS)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                systemConfigService.update(SYSTEM_CONFIG_LINK_TYPE, form.getType());
                applicationContext.publishEvent(new AdminLinkSettingsEvent(currentUser().orElseThrow(RuntimeException::new)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

}
