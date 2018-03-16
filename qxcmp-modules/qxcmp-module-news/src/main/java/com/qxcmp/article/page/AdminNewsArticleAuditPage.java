package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
import com.qxcmp.article.form.AdminNewsArticleAuditForm;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.segment.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminNewsArticleAuditPage extends AbstractNewsPage {

    private final Article article;
    private final AdminNewsArticleAuditForm form;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_NEWS, "");
        addComponent(new TextContainer()
                .addComponent(new Segment().addComponent(viewHelper.nextForm(form, null)))
                .addComponent(getArticleDetailsOverview(article)));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "文章管理", "", "审核文章");
    }
}
