package com.qxcmp.article.event;

import com.qxcmp.article.Article;
import com.qxcmp.user.User;

/**
 * @author Aaric
 */
public class ArticleEnableEvent extends BaseArticleStatusChangeEvent {
    public ArticleEnableEvent(User target, Article article) {
        super(target, article);
    }
}
