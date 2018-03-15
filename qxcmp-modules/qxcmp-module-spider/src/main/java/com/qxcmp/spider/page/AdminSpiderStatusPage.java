package com.qxcmp.spider.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminEntityTablePage;
import com.qxcmp.spider.SpiderRuntime;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.spider.SpiderModule.ADMIN_SPIDER_URL;
import static com.qxcmp.spider.SpiderModuleNavigation.ADMIN_MENU_SPIDER;
import static com.qxcmp.spider.SpiderModuleNavigation.ADMIN_MENU_SPIDER_STATUS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminSpiderStatusPage extends AbstractQxcmpAdminEntityTablePage {

    private final Page<SpiderRuntime> spiderRuntimes;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_SPIDER, ADMIN_MENU_SPIDER_STATUS);
        return viewHelper.nextEntityTable(SpiderRuntime.class, spiderRuntimes, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("蜘蛛管理", ADMIN_SPIDER_URL, "运行状态");
    }

}
