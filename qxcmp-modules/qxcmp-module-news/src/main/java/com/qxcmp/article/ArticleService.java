package com.qxcmp.article;

import com.qxcmp.article.event.*;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.user.User;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

/**
 * 文章服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class ArticleService extends AbstractEntityService<Article, Long, ArticleRepository> {

    private final ApplicationContext applicationContext;

    public Long countByStatus(ArticleStatus status) {
        return repository.countByStatus(status);
    }

    public Long countByUserIdAndStatus(String userId, ArticleStatus status) {
        return repository.countByUserIdAndStatus(userId, status);
    }

    public Page<Article> findByStatus(ArticleStatus status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    public Page<Article> findByChannel(Channel channel, Pageable pageable) {
        return repository.findByChannelsContains(channel, pageable);
    }

    public Page<Article> findByChannelAndStatus(Channel channel, ArticleStatus status, Pageable pageable) {
        return repository.findByChannelsAndStatus(channel, status, pageable);
    }

    public Page<Article> findByChannelContainsAndStatus(Set<Channel> channels, ArticleStatus status, Pageable pageable) {
        return repository.findByChannelsContainingAndStatus(channels, status, pageable);
    }

    public Page<Article> findByUserId(String userId, Pageable pageable) {
        return repository.findByUserIdOrderByDateModifiedDesc(userId, pageable);
    }

    public Page<Article> findByUserIdAndStatus(String userId, ArticleStatus status, Pageable pageable) {
        return repository.findByUserIdAndStatusOrderByDateModifiedDesc(userId, status, pageable);
    }

    public Page<Article> findByChannelsAndStatuses(Set<Channel> channels, Set<ArticleStatus> statuses, Pageable pageable) {
        return repository.findByChannelsAndStatuses(channels, statuses, pageable);
    }

    /**
     * 用户申请审核文章事件
     *
     * @param user    申请用户
     * @param article 申请文章
     * @param request 申请原因
     */
    public void audit(User user, Article article, String request) {
        checkState(article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT));
        applicationContext.publishEvent(new ArticleAuditingEvent(user, update(article.getId(), a -> {
            a.setAuditRequest(request);
            a.setDateAuditing(new Date());
            a.setStatus(ArticleStatus.AUDITING);
        })));
    }

    /**
     * 驳回一篇文章
     *
     * @param user    驳回的用户
     * @param article 驳回的文章
     * @param reason  驳回原因
     */
    public void reject(User user, Article article, String reason) {
        checkState(Objects.equals(article.getStatus(), ArticleStatus.AUDITING));
        applicationContext.publishEvent(new ArticleRejectEvent(user, update(article.getId(), a -> {
            a.setAuditor(user.getId());
            a.setAuditResponse(reason);
            a.setDateRejected(DateTime.now().toDate());
            a.setStatus(ArticleStatus.REJECT);
        }), reason));
    }

    /**
     * 发布一篇文章
     *
     * @param user     发布的用户
     * @param article  发布的文章
     * @param comments 发布备注
     */
    public void publish(User user, Article article, String comments) {
        checkState(Objects.equals(article.getStatus(), ArticleStatus.AUDITING));
        applicationContext.publishEvent(new ArticlePublishEvent(user, update(article.getId(), a -> {
            a.setAuditor(user.getId());
            a.setAuditResponse(comments);
            a.setDatePublished(new Date());
            a.setStatus(ArticleStatus.PUBLISHED);
        })));
    }

    public void enable(User user, Article article) {
        checkState(Objects.equals(article.getStatus(), ArticleStatus.DISABLED));
        applicationContext.publishEvent(new ArticleEnableEvent(user, update(article.getId(), a -> {
            a.setDatePublished(DateTime.now().toDate());
            a.setStatus(ArticleStatus.PUBLISHED);
            a.setAuditor(user.getId());
        })));
    }

    public void disable(User user, Article article) {
        checkState(Objects.equals(article.getStatus(), ArticleStatus.PUBLISHED));
        applicationContext.publishEvent(new ArticleDisableEvent(user, update(article.getId(), a -> {
            a.setDateDisabled(DateTime.now().toDate());
            a.setStatus(ArticleStatus.DISABLED);
            a.setDisableUser(user.getId());
        })));
    }

    @Override
    public Article create(Article entity) {
        entity.setStatus(ArticleStatus.NEW);
        entity.setDateCreated(new Date());
        entity.setDateModified(new Date());
        return super.create(entity);
    }
}
