package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.article.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.article.NewsModuleNavigation.*;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminNewsPage extends AbstractNewsPage {

    private final ArticleService articleService;
    private final ChannelService channelService;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_NEWS, "");
        setMenuBadge(ADMIN_MENU_NEWS_ARTICLE_AUDITING, articleService.countByStatus(ArticleStatus.AUDITING));
        setMenuBadge(ADMIN_MENU_NEWS_ARTICLE_PUBLISHED, articleService.countByStatus(ArticleStatus.PUBLISHED));
        setMenuBadge(ADMIN_MENU_NEWS_ARTICLE_DISABLED, articleService.countByStatus(ArticleStatus.DISABLED));
        setMenuBadge(ADMIN_MENU_NEWS_USER_CHANNEL, (long) channelService.findByUser(userService.currentUser()).size());
        setMenuBadge(ADMIN_MENU_NEWS_CHANNEL, channelService.count());
        addComponent(viewHelper.nextOverview("新闻管理"));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理");
    }
}
