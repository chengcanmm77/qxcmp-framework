package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.user.User;
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
public abstract class AbstractUserArticleTablePage extends AbstractNewsPage {

    private final ArticleService articleService;
    private final Page<Article> articles;
    private final User user;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_USER_ARTICLE, getActiveMenuId());
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_DRAFT, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.NEW));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_AUDITING, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.AUDITING));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_REJECTED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.REJECT));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_PUBLISHED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.PUBLISHED));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_DISABLED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.DISABLED));
        addComponent(new Segment().addComponent(viewHelper.nextEntityTable(getEntityTableName(), Article.class, articles, request)));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "我的文章", ADMIN_NEWS_URL + "/user/article");
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
