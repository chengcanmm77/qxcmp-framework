package com.qxcmp.article.event;

import com.qxcmp.article.Article;
import com.qxcmp.user.User;

/**
 * 用户申请文章审核事件
 *
 * @author Aaric
 */
public class ArticleAuditingEvent extends BaseArticleStatusChangeEvent {
    public ArticleAuditingEvent(User target, Article article) {
        super(target, article);
    }
}
