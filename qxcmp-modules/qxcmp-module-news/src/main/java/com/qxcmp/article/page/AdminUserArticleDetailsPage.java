package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.html.HtmlText;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.support.Wide;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleNavigation.*;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminUserArticleDetailsPage extends AbstractNewsPage {

    private final ArticleService articleService;
    private final User user;
    private final Article article;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_USER_ARTICLE, "");
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_DRAFT, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.NEW));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_AUDITING, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.AUDITING));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_REJECTED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.REJECT));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_PUBLISHED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.PUBLISHED));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_DISABLED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.DISABLED));
        addComponent(viewHelper.nextOverview(article.getTitle(), article.getAuthor())
                .addComponent(new VerticallyDividedGrid().setVerticallyPadded()
                        .addItem(new Row()
                                .addCol(new Col().setComputerWide(Wide.FOUR).setMobileWide(Wide.SIXTEEN).addComponent(new Image(article.getCover()).setCentered().setBordered().setRounded()))
                                .addCol(new Col().setComputerWide(Wide.TWELVE).setMobileWide(Wide.SIXTEEN).addComponent(getArticleDetailsTable(article))))
                        .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new HtmlText(article.getContent())))))
                .addLink("我的文章", ADMIN_NEWS_URL + "/user/article")
                .addLink("草稿箱", ADMIN_NEWS_URL + "/user/article/draft")
                .addLink("新建文章", ADMIN_NEWS_URL + "/user/article/new")
        );
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "我的文章", ADMIN_NEWS_URL + "/user/article", "文章预览");
    }

}
