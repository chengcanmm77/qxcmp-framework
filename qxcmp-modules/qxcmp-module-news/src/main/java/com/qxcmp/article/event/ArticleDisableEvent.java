package com.qxcmp.article.event;

import com.qxcmp.article.Article;
import com.qxcmp.user.User;

/**
 * 文章禁用事件
 *
 * @author Aaric
 */
public class ArticleDisableEvent extends BaseArticleStatusChangeEvent {
    public ArticleDisableEvent(User target, Article article) {
        super(target, article);
    }
}
