package com.qxcmp.web.controller;

import com.qxcmp.audit.ActionException;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.finance.DepositOrderService;
import com.qxcmp.finance.WalletService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminFinanceWalletForm;
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

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_ADMIN_URL + "/finance")
@RequiredArgsConstructor
public class AdminFinancePageController extends QxcmpController {
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
}

