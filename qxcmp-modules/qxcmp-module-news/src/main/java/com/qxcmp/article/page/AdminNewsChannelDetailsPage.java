package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Channel;
import com.qxcmp.web.view.elements.html.HtmlText;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
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
public class AdminNewsChannelDetailsPage extends AbstractNewsPage {

    private final Channel channel;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_ARTICLE, ADMIN_MENU_ARTICLE_CHANNEL);
        addComponent(viewHelper.nextOverview(channel.getName())
                .addComponent(new HtmlText(channel.getContent()))
                .addLink("返回", ADMIN_NEWS_URL + "/channel")
        );
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "栏目管理", ADMIN_NEWS_URL + "/channel", "栏目预览");
    }
}
