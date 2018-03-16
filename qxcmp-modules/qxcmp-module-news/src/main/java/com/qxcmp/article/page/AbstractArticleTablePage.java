package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.web.view.elements.segment.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleNavigation.*;

/**
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractArticleTablePage extends AbstractNewsPage {

    private final ArticleService articleService;
    private final Page<Article> articles;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_NEWS, getActiveMenuId());
        setMenuBadge(ADMIN_MENU_NEWS_ARTICLE_AUDITING, articleService.countByStatus(ArticleStatus.AUDITING));
        setMenuBadge(ADMIN_MENU_NEWS_ARTICLE_PUBLISHED, articleService.countByStatus(ArticleStatus.PUBLISHED));
        setMenuBadge(ADMIN_MENU_NEWS_ARTICLE_DISABLED, articleService.countByStatus(ArticleStatus.DISABLED));
        addComponent(new Segment().addComponent(viewHelper.nextEntityTable(getEntityTableName(), Article.class, articles, request)));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "文章管理");
    }

    /**
     * 当前页面激活菜单ID
     *
     * @return 当前页面激活菜单ID
     */
    protected abstract String getActiveMenuId();

    /**
     * 文章实体表格名称
     *
     * @return 文章实体表格名称
     */
    protected abstract String getEntityTableName();
}
