package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
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
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS_USER_CHANNEL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminUserChannelArticleDetailsPage extends AbstractNewsPage {

    private final Article article;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_NEWS, ADMIN_MENU_NEWS_USER_CHANNEL);
        addComponent(viewHelper.nextOverview(article.getTitle(), article.getAuthor())
                .addComponent(new VerticallyDividedGrid().setVerticallyPadded()
                        .addItem(new Row()
                                .addCol(new Col().setComputerWide(Wide.FOUR).setMobileWide(Wide.SIXTEEN).addComponent(new Image(article.getCover()).setCentered().setBordered().setRounded()))
                                .addCol(new Col().setComputerWide(Wide.TWELVE).setMobileWide(Wide.SIXTEEN).addComponent(getArticleDetailsTable(article))))
                        .addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(new HtmlText(article.getContent())))))
                .addLink("返回", ADMIN_NEWS_URL + "/user/channel")
        );
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "我的栏目", ADMIN_NEWS_URL + "/user/channel", "栏目文章预览");
    }
}
