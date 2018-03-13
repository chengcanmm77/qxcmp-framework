package com.qxcmp.redeem.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.audit.ActionException;
import com.qxcmp.redeem.RedeemKey;
import com.qxcmp.redeem.RedeemKeyService;
import com.qxcmp.redeem.RedeemModuleSystemConfig;
import com.qxcmp.redeem.event.AdminRedeemGenerateEvent;
import com.qxcmp.redeem.extension.RedeemExtensionPoint;
import com.qxcmp.redeem.form.AdminRedeemGenerateForm;
import com.qxcmp.redeem.form.AdminRedeemSettingsForm;
import com.qxcmp.redeem.page.AdminRedeemDetailsPage;
import com.qxcmp.redeem.page.AdminRedeemGeneratePage;
import com.qxcmp.redeem.page.AdminRedeemSettingsPage;
import com.qxcmp.redeem.page.AdminRedeemTablePage;
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
        return entityDetailsPage(AdminRedeemDetailsPage.class, id, redeemKeyService);
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
            try {
                List<RedeemKey> redeemKeys = redeemKeyService.generate(form.getType(), form.getContent(), form.getDateExpired(), form.getQuantity());
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
