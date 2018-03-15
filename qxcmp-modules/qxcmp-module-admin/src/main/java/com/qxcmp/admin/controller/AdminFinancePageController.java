package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.page.AdminFinanceDepositTablePage;
import com.qxcmp.admin.page.AdminFinanceOverviewPage;
import com.qxcmp.admin.page.AdminFinanceWalletTablePage;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.finance.DepositOrderService;
import com.qxcmp.finance.WalletRecordService;
import com.qxcmp.finance.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_FINANCE_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_FINANCE_URL)
@RequiredArgsConstructor
public class AdminFinancePageController extends QxcmpAdminController {

    private final WalletService walletService;
    private final WalletRecordService walletRecordService;
    private final DepositOrderService depositOrderService;

    @GetMapping("")
    public ModelAndView overview() {
        return page(AdminFinanceOverviewPage.class, walletService.findAll(), walletRecordService.findAll(), depositOrderService.findFinishedOrder());
    }

    @GetMapping("/deposit")
    public ModelAndView depositTable(Pageable pageable) {
        Page<DepositOrder> orders = depositOrderService.findFinishedOrder(pageable);
        return page(AdminFinanceDepositTablePage.class, orders);
    }

    @GetMapping("/wallet")
    public ModelAndView walletTable(Pageable pageable) {
        return page(AdminFinanceWalletTablePage.class, walletService, pageable);
    }
}

