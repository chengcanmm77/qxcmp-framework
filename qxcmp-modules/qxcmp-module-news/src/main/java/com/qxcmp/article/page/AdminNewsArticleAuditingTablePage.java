package com.qxcmp.article.page;

import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleService;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS_ARTICLE_AUDITING;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminNewsArticleAuditingTablePage extends AbstractArticleTablePage {
    public AdminNewsArticleAuditingTablePage(ArticleService articleService, Page<Article> articles) {
        super(articleService, articles);
    }

    @Override
    protected String getActiveMenuId() {
        return ADMIN_MENU_NEWS_ARTICLE_AUDITING;
    }

    @Override
    protected String getEntityTableName() {
        return "auditing";
    }
}
