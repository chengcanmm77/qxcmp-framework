package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminEntityTablePage;
import com.qxcmp.article.ChannelService;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_ARTICLE;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_ARTICLE_CHANNEL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminNewsChannelTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final ChannelService channelService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_ARTICLE, ADMIN_MENU_ARTICLE_CHANNEL);
        return viewHelper.nextEntityTable("admin", pageable, channelService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "栏目管理");
    }
}
