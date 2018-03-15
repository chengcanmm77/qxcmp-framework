package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.finance.DepositOrder;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_FINANCE_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_FINANCE;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_FINANCE_DEPOSIT;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminFinanceDepositTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final Page<DepositOrder> orders;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_FINANCE, ADMIN_MENU_FINANCE_DEPOSIT);
        return viewHelper.nextEntityTable(DepositOrder.class, orders, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("财务统计", ADMIN_FINANCE_URL, "充值订单");
    }
}
