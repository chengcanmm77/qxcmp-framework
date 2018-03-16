package com.qxcmp.article.page;

import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.article.form.AdminNewsUserArticleNewForm;
import com.qxcmp.user.User;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.segment.Segment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import static com.qxcmp.article.NewsModuleNavigation.*;

/**
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractUserArticleFormPage extends AbstractNewsPage {

    private final AdminNewsUserArticleNewForm form;
    private final BindingResult bindingResult;
    private ArticleService articleService;

    @Override
    public void render() {
        User user = userService.currentUser();
        setMenu(ADMIN_MENU_USER_ARTICLE, "");
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_DRAFT, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.NEW));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_AUDITING, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.AUDITING));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_REJECTED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.REJECT));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_PUBLISHED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.PUBLISHED));
        setMenuBadge(ADMIN_MENU_USER_ARTICLE_DISABLED, articleService.countByUserIdAndStatus(user.getId(), ArticleStatus.DISABLED));
        addComponent(new TextContainer().addComponent(new Segment().addComponent(viewHelper.nextForm(form, bindingResult))));
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

}
