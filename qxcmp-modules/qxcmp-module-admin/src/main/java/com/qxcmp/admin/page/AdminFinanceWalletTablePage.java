package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.finance.WalletService;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_FINANCE_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_FINANCE;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_FINANCE_WALLET;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminFinanceWalletTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final WalletService walletService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_FINANCE, ADMIN_MENU_FINANCE_WALLET);
        return viewHelper.nextEntityTable(pageable, walletService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("财务统计", ADMIN_FINANCE_URL, "用户钱包");
    }
}
