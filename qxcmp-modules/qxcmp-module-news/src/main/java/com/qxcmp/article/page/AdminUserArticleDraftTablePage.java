package com.qxcmp.article.page;

import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleService;
import com.qxcmp.user.User;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_USER_ARTICLE_DRAFT;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminUserArticleDraftTablePage extends AbstractUserArticleTablePage {

    public AdminUserArticleDraftTablePage(ArticleService articleService, Page<Article> articles, User user) {
        super(articleService, articles, user);
    }

    @Override
    protected String getActiveMenuId() {
        return ADMIN_MENU_USER_ARTICLE_DRAFT;
    }

    @Override
    protected String getEntityTableName() {
        return "userDraft";
    }
}
