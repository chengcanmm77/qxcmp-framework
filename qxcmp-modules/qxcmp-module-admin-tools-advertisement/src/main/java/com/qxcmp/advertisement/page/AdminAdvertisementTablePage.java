package com.qxcmp.advertisement.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminEntityTablePage;
import com.qxcmp.advertisement.AdvertisementService;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminAdvertisementTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final AdvertisementService advertisementService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        return viewHelper.nextEntityTable(pageable, advertisementService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("控制台", "", "系统工具", "tools", "广告管理");
    }
}
