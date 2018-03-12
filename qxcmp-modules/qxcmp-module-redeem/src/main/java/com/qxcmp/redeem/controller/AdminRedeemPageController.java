package com.qxcmp.redeem.controller;

import com.google.common.collect.Lists;
import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.audit.ActionException;
import com.qxcmp.redeem.RedeemKey;
import com.qxcmp.redeem.RedeemKeyService;
import com.qxcmp.redeem.RedeemModuleSystemConfig;
import com.qxcmp.redeem.event.AdminRedeemGenerateEvent;
import com.qxcmp.redeem.extension.RedeemExtensionPoint;
import com.qxcmp.redeem.form.AdminRedeemGenerateForm;
import com.qxcmp.redeem.form.AdminRedeemSettingsForm;
import com.qxcmp.redeem.page.AdminRedeemGeneratePage;
import com.qxcmp.redeem.page.AdminRedeemSettingsPage;
import com.qxcmp.redeem.page.AdminRedeemTablePage;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.NAVIGATION_ADMIN_REDEEM;
import static com.qxcmp.core.QxcmpNavigationConfiguration.NAVIGATION_ADMIN_REDEEM_MANAGEMENT;
import static com.qxcmp.redeem.RedeemModule.ADMIN_REDEEM_URL;
import static com.qxcmp.redeem.RedeemModuleSystemConfig.DEFAULT_EXPIRE_DURATION;
import static com.qxcmp.redeem.RedeemModuleSystemConfig.DEFAULT_EXPIRE_DURATION_DEFAULT;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_REDEEM_URL)
@RequiredArgsConstructor
public class AdminRedeemPageController extends QxcmpAdminController {

    private final RedeemKeyService redeemKeyService;
    private final RedeemExtensionPoint redeemExtensionPoint;

    @GetMapping("")
    public ModelAndView redeemPage(Pageable pageable) {
        return page(AdminRedeemTablePage.class, redeemKeyService, pageable);
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
                        })).addLink("返回", QXCMP_ADMIN_URL + "/redeem")))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "兑换码管理", "redeem", "兑换码详情")
                .setVerticalNavigation(NAVIGATION_ADMIN_REDEEM, NAVIGATION_ADMIN_REDEEM_MANAGEMENT)
                .build()).orElse(page(new Overview(new IconHeader("兑换码不存在", new Icon("warning circle"))).addLink("返回", QXCMP_ADMIN_URL + "/redeem")).build());
    }

    @GetMapping("/generate")
    public ModelAndView generateGet(final AdminRedeemGenerateForm form, BindingResult bindingResult) {
        form.setDateExpired(DateTime.now().plusSeconds(systemConfigService.getInteger(DEFAULT_EXPIRE_DURATION).orElse(DEFAULT_EXPIRE_DURATION_DEFAULT)).toDate());
        return page(AdminRedeemGeneratePage.class, form, bindingResult).addObject("selection_items_type", redeemExtensionPoint.getTypes());
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/generate")
    public ModelAndView generatePost(@Valid final AdminRedeemGenerateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return generateGet(form, bindingResult);
        }
        return execute("生成兑换码", context -> {
            List<RedeemKey> redeemKeys = Lists.newArrayList();
            try {
                for (int i = 0; i < form.getQuantity(); i++) {
                    redeemKeys.add(redeemKeyService.create(() -> {
                        RedeemKey next = redeemKeyService.next();
                        next.setType(form.getType());
                        next.setContent(form.getContent());
                        next.setDateExpired(form.getDateExpired());
                        return next;
                    }));
                }
                context.put("keys", redeemKeys);
                applicationContext.publishEvent(new AdminRedeemGenerateEvent(currentUser().orElseThrow(RuntimeException::new), form.getQuantity(), form.getType(), form.getContent()));
            } catch (Exception e) {
                throw new ActionException(e.getMessage());
            }
        }, (context, overview) -> overview.addComponent(viewHelper.nextTable(stringObjectMap -> {
            if (Objects.nonNull(context.get("keys"))) {
                List<RedeemKey> redeemKeys = (List<RedeemKey>) context.get("keys");
                stringObjectMap.put("业务名称", form.getType());
                stringObjectMap.put("业务数据", form.getContent());
                for (int i = 0; i < redeemKeys.size(); i++) {
                    RedeemKey redeemKey = redeemKeys.get(i);
                    stringObjectMap.put(String.valueOf(i + 1), redeemKey.getId());
                }
            }
        })));
    }

    @GetMapping("/settings")
    public ModelAndView settingsGet(final AdminRedeemSettingsForm form, BindingResult bindingResult) {
        return systemConfigPage(AdminRedeemSettingsPage.class, form, bindingResult, RedeemModuleSystemConfig.class);
    }

    @PostMapping("/settings")
    public ModelAndView settingsPost(@Valid final AdminRedeemSettingsForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return settingsGet(form, bindingResult);
        }
        return updateSystemConfig(RedeemModuleSystemConfig.class, form);
    }
}
