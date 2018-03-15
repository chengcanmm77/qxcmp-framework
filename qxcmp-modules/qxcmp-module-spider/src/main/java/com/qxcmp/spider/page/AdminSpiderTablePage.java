package com.qxcmp.spider.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminEntityTablePage;
import com.qxcmp.spider.SpiderDefinition;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.spider.SpiderModuleNavigation.ADMIN_MENU_SPIDER;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminSpiderTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final Page<SpiderDefinition> spiderDefinitions;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_SPIDER, "");
        return viewHelper.nextEntityTable(SpiderDefinition.class, spiderDefinitions, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("蜘蛛管理");
    }

}
