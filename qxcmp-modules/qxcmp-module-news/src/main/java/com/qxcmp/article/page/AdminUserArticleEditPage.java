package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.form.AdminNewsUserArticleNewForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
public class AdminUserArticleEditPage extends AbstractUserArticleFormPage {

    public AdminUserArticleEditPage(AdminNewsUserArticleNewForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "我的文章", ADMIN_NEWS_URL + "/user/article", "编辑文章");
    }
}
