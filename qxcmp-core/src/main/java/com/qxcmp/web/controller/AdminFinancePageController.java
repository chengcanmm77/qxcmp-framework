package com.qxcmp.web.controller;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.google.common.collect.ImmutableList;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.finance.DepositOrderService;
import com.qxcmp.finance.WalletService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminFinanceWalletForm;
import com.qxcmp.web.form.AdminFinanceWeixinForm;
import com.qxcmp.web.view.elements.segment.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/finance")
@RequiredArgsConstructor
public class AdminFinancePageController extends QxcmpController {

    private static final List<String> SUPPORT_WEIXIN_PAYMENT = ImmutableList.of("NATIVE", "JSAPI");

    private final WxPayConfig wxPayConfig;
    private final WalletService walletService;
    private final DepositOrderService depositOrderService;

    @GetMapping("")
    public ModelAndView financePage() {
        return page()
                .setBreadcrumb("控制台", "", "财务管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_FINANCE, "")
                .build();
    }

    @GetMapping("/deposit")
    public ModelAndView depositPage(Pageable pageable) {

        Page<DepositOrder> finishedOrder = depositOrderService.findFinishedOrder(pageable);

        return page().addComponent(convertToTable(DepositOrder.class, finishedOrder))
                .setBreadcrumb("控制台", "", "财务管理", "finance", "充值订单管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_FINANCE, NAVIGATION_ADMIN_FINANCE_DEPOSIT)
                .build();
    }

    @GetMapping("/wallet")
    public ModelAndView financeWalletPage(final AdminFinanceWalletForm form) {
        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "财务管理", "finance", "用户钱包管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_FINANCE, NAVIGATION_ADMIN_FINANCE_WALLET_MANAGEMENT)
                .build();
    }

    @PostMapping("/wallet")
    public ModelAndView financeWalletPage(@Valid final AdminFinanceWalletForm form, BindingResult bindingResult) {

        if (!userService.findOne(form.getUserId()).isPresent()) {
            bindingResult.rejectValue("userId", "", "用户ID不存在");
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "财务管理", "finance", "用户钱包管理")
                    .setVerticalNavigation(NAVIGATION_ADMIN_FINANCE, NAVIGATION_ADMIN_FINANCE_WALLET_MANAGEMENT)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                walletService.changeBalance(form.getUserId(), form.getAmount(), form.getComments(), "");
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @GetMapping("/weixin")
    public ModelAndView weixinPayPage(final AdminFinanceWeixinForm form) {

        form.setEnable(systemConfigService.getBoolean(QxcmpSystemConfig.WECHAT_PAYMENT_ENABLE).orElse(QxcmpSystemConfig.WECHAT_PAYMENT_ENABLE_DEFAULT));
        form.setTradeType(systemConfigService.getString(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE).orElse(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE_DEFAULT));
        form.setAppId(systemConfigService.getString(QxcmpSystemConfig.WECHAT_APP_ID).orElse(""));
        form.setMchId(systemConfigService.getString(QxcmpSystemConfig.WECHAT_MCH_ID).orElse(""));
        form.setMchKey(systemConfigService.getString(QxcmpSystemConfig.WECHAT_MCH_KEY).orElse(""));
        form.setNotifyUrl(systemConfigService.getString(QxcmpSystemConfig.WECHAT_NOTIFY_URL).orElse(""));
        form.setSubAppId(systemConfigService.getString(QxcmpSystemConfig.WECHAT_SUB_APP_ID).orElse(""));
        form.setSubMchId(systemConfigService.getString(QxcmpSystemConfig.WECHAT_SUB_MCH_ID).orElse(""));
        form.setKeyPath(systemConfigService.getString(QxcmpSystemConfig.WECHAT_KEY_PATH).orElse(""));

        return page()
                .addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "财务管理", "finance", "微信支付配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_FINANCE, NAVIGATION_ADMIN_FINANCE_WEIXIN_SETTINGS)
                .addObject("selection_items_tradeType", SUPPORT_WEIXIN_PAYMENT)
                .build();
    }

    @PostMapping("/weixin")
    public ModelAndView weixinPayPage(@Valid final AdminFinanceWeixinForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "财务管理", "finance", "微信支付配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_FINANCE, NAVIGATION_ADMIN_FINANCE_WEIXIN_SETTINGS)
                    .addObject("selection_items_tradeType", SUPPORT_WEIXIN_PAYMENT)
                    .build();
        }

        return submitForm(form, context -> {
            systemConfigService.update(QxcmpSystemConfig.WECHAT_PAYMENT_ENABLE, String.valueOf(form.isEnable()));
            systemConfigService.update(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE, form.getTradeType());
            systemConfigService.update(QxcmpSystemConfig.WECHAT_APP_ID, form.getAppId());
            systemConfigService.update(QxcmpSystemConfig.WECHAT_MCH_ID, form.getMchId());
            systemConfigService.update(QxcmpSystemConfig.WECHAT_MCH_KEY, form.getMchKey());
            systemConfigService.update(QxcmpSystemConfig.WECHAT_SUB_APP_ID, form.getSubAppId());
            systemConfigService.update(QxcmpSystemConfig.WECHAT_SUB_MCH_ID, form.getSubMchId());
            systemConfigService.update(QxcmpSystemConfig.WECHAT_NOTIFY_URL, form.getNotifyUrl());
            systemConfigService.update(QxcmpSystemConfig.WECHAT_KEY_PATH, form.getKeyPath());
            systemConfigService.update(QxcmpSystemConfig.FINANCE_PAYMENT_SUPPORT_WEIXIN, systemConfigService.getBoolean(QxcmpSystemConfig.WECHAT_PAYMENT_ENABLE).orElse(QxcmpSystemConfig.WECHAT_PAYMENT_ENABLE_DEFAULT).toString());

            wxPayConfig.setAppId(form.getAppId());
            wxPayConfig.setMchId(form.getMchId());
            wxPayConfig.setMchKey(form.getMchKey());
            wxPayConfig.setSubAppId(form.getSubAppId());
            wxPayConfig.setSubMchId(form.getSubMchId());
            wxPayConfig.setNotifyUrl(form.getNotifyUrl());
            wxPayConfig.setKeyPath(form.getKeyPath());
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/finance/weixin"));
    }
}
