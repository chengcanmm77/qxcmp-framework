package com.qxcmp.article;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 文章服务
 *
 * @author aaric
 */
@Service
public class ArticleService extends AbstractEntityService<Article, Long, ArticleRepository> {

    public Optional<Article> findOne(String id) {
        try {
            Long aId = Long.parseLong(id);
            return findOne(aId);
        } catch (Exception e) {
            return Optional.empty();
        }
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
        return repository.findByUserId(userId, pageable);
    }

    public Page<Article> findByUserIdAndStatus(String userId, ArticleStatus status, Pageable pageable) {
        return repository.findByUserIdAndStatus(userId, status, pageable);
    }

    public Page<Article> findByChannelsAndStatuses(Set<Channel> channels, Set<ArticleStatus> statuses, Pageable pageable) {
        return repository.findByChannelsAndStatuses(channels, statuses, pageable);
    }

    @Override
    public Article create(Supplier<Article> supplier) {

        Article entity = supplier.get();

        if (Objects.nonNull(entity.getId())) {
            return null;
        }

        entity.setDateCreated(new Date());
        entity.setDateModified(new Date());

        return super.create(() -> entity);
    }
}
