package com.qxcmp.web.controller;

import com.google.common.collect.Lists;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.QxcmpSystemConfigConfiguration;
import com.qxcmp.core.event.AdminRedeemGenerateEvent;
import com.qxcmp.core.event.AdminRedeemSettingsEvent;
import com.qxcmp.redeem.RedeemKey;
import com.qxcmp.redeem.RedeemKeyService;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminRedeemGenerateForm;
import com.qxcmp.web.form.AdminRedeemSettingsForm;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
import static com.qxcmp.core.QxcmpSystemConfigConfiguration.*;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/redeem")
@RequiredArgsConstructor
public class AdminRedeemPageController extends QxcmpController {

    private final RedeemKeyService redeemKeyService;

    @GetMapping("")
    public ModelAndView redeemPage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, redeemKeyService))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_MANAGEMENT)
                .build();
    }

    @GetMapping("/{id}/details")
    public ModelAndView redeemDetailsPage(@PathVariable String id) {
        return redeemKeyService.findOne(id).map(redeemKey -> page()
                .addComponent(new TextContainer().addComponent(new Overview("兑换码详情")
                        .addComponent(convertToTable(stringObjectMap -> {
                            stringObjectMap.put("ID", redeemKey.getId());
                            stringObjectMap.put("用户ID", redeemKey.getUserId());
                            stringObjectMap.put("用户名", userService.findOne(redeemKey.getUserId()).map(User::getUsername).orElse(""));
                            stringObjectMap.put("业务名称", redeemKey.getType());
                            stringObjectMap.put("业务数据", redeemKey.getContent());
                            stringObjectMap.put("状态", redeemKey.getStatus().getValue());
                            stringObjectMap.put("使用时间", redeemKey.getDateUsed());
                            stringObjectMap.put("创建时间", redeemKey.getDateCreated());
                            stringObjectMap.put("过期时间", redeemKey.getDateExpired());
                        })).addLink("返回", QXCMP_BACKEND_URL + "/redeem")))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "兑换码详情")
                .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_MANAGEMENT)
                .build()).orElse(page(new Overview(new IconHeader("兑换码不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/redeem")).build());
    }

    @GetMapping("/generate")
    public ModelAndView redeemGeneratePage(final AdminRedeemGenerateForm form) {

        form.setDateExpired(new Date(System.currentTimeMillis() + 1000 * systemConfigService.getInteger(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_REDEEM_DEFAULT_EXPIRE_DURATION).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_REDEEM_DEFAULT_EXPIRE_DURATION_DEFAULT_VALUE)));
        form.setQuantity(1);

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "生成兑换码")
                .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_MANAGEMENT)
                .addObject("selection_items_type", systemConfigService.getList(SYSTEM_CONFIG_REDEEM_TYPE_LIST))
                .build();
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/generate")
    public ModelAndView redeemGeneratePage(@Valid final AdminRedeemGenerateForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "生成兑换码")
                    .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_MANAGEMENT)
                    .addObject("selection_items_type", systemConfigService.getList(SYSTEM_CONFIG_REDEEM_TYPE_LIST))
                    .build();
        }

        return submitForm(form, context -> {

            List<RedeemKey> redeemKeys = Lists.newArrayList();

            try {
                for (int i = 0; i < form.getQuantity(); i++) {
                    redeemKeys.add(redeemKeyService.create(() -> {
                        RedeemKey redeemKey = redeemKeyService.next();
                        redeemKey.setType(form.getType());
                        redeemKey.setContent(form.getContent());
                        redeemKey.setDateExpired(form.getDateExpired());
                        return redeemKey;
                    }));
                }

                context.put("keys", redeemKeys);

                applicationContext.publishEvent(new AdminRedeemGenerateEvent(currentUser().orElseThrow(RuntimeException::new), form.getType(), form.getQuantity()));
            } catch (Exception e) {
                throw new ActionException(e.getMessage());
            }
        }, (context, overview) -> overview.addComponent(convertToTable(stringObjectMap -> {
            if (Objects.nonNull(context.get("keys"))) {
                List<RedeemKey> redeemKeys = (List<RedeemKey>) context.get("keys");

                stringObjectMap.put("业务名称", form.getType());
                stringObjectMap.put("业务数据", form.getContent());

                for (int i = 0; i < redeemKeys.size(); i++) {
                    RedeemKey redeemKey = redeemKeys.get(i);
                    stringObjectMap.put(String.valueOf(i + 1), redeemKey.getId());
                }
            }
        })).addLink("返回", QXCMP_BACKEND_URL + "/redeem").addLink("继续生成", QXCMP_BACKEND_URL + "/redeem/generate"));
    }

    @GetMapping("/settings")
    public ModelAndView redeemSettingsPage(final AdminRedeemSettingsForm form) {

        form.setEnable(systemConfigService.getBoolean(SYSTEM_CONFIG_REDEEM_ENABLE).orElse(SYSTEM_CONFIG_REDEEM_ENABLE_DEFAULT_VALUE));
        form.setExpireDuration(systemConfigService.getInteger(SYSTEM_CONFIG_REDEEM_DEFAULT_EXPIRE_DURATION).orElse(SYSTEM_CONFIG_REDEEM_DEFAULT_EXPIRE_DURATION_DEFAULT_VALUE));
        form.setType(systemConfigService.getList(SYSTEM_CONFIG_REDEEM_TYPE_LIST));

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "兑换码配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_SETTINGS)
                .build();
    }

    @PostMapping("/settings")
    public ModelAndView redeemSettingsPage(@Valid final AdminRedeemSettingsForm form, BindingResult bindingResult,
                                           @RequestParam(value = "add_type", required = false) boolean addType,
                                           @RequestParam(value = "remove_type", required = false) Integer removeType) {

        if (addType) {
            form.getType().add("");
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "兑换码配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_SETTINGS)
                    .build();
        }

        if (Objects.nonNull(removeType)) {
            form.getType().remove(removeType.intValue());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "兑换码配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_SETTINGS)
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "兑换码配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_SETTINGS)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                systemConfigService.update(SYSTEM_CONFIG_REDEEM_ENABLE, String.valueOf(form.isEnable()));
                systemConfigService.update(SYSTEM_CONFIG_REDEEM_DEFAULT_EXPIRE_DURATION, String.valueOf(form.getExpireDuration()));
                systemConfigService.update(SYSTEM_CONFIG_REDEEM_TYPE_LIST, form.getType());

                applicationContext.publishEvent(new AdminRedeemSettingsEvent(currentUser().orElseThrow(RuntimeException::new)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }
}
