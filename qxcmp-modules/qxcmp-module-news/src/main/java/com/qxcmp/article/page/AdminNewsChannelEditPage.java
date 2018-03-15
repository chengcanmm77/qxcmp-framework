package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminFormPage;
import com.qxcmp.article.form.AdminNewsChannelEditForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

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
public class AdminNewsChannelEditPage extends AbstractQxcmpAdminFormPage<AdminNewsChannelEditForm> {
    public AdminNewsChannelEditPage(AdminNewsChannelEditForm form, BindingResult bindingResult) {
        super(form, bindingResult);
    }

    @Override
    protected void postRender() {
        setMenu(ADMIN_MENU_ARTICLE, ADMIN_MENU_ARTICLE_CHANNEL);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "栏目管理", ADMIN_NEWS_URL + "/channel", "编辑栏目");
    }
}
