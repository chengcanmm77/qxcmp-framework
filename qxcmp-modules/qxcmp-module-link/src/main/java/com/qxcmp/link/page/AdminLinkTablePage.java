package com.qxcmp.link.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminEntityTablePage;
import com.qxcmp.link.LinkService;
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
public class AdminLinkTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final LinkService linkService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        return viewHelper.nextEntityTable(pageable, linkService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("链接管理");
    }
}
