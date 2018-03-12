package com.qxcmp.redeem.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminEntityTablePage;
import com.qxcmp.redeem.RedeemKeyService;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Aaric
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminRedeemTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final RedeemKeyService redeemKeyService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        return viewHelper.nextEntityTable(pageable, redeemKeyService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("兑换码管理");
    }
}
