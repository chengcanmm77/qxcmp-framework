package com.qxcmp.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    /**
     * 获取某个状态的文章数量
     *
     * @param status 文章状态
     * @return 某个状态的文章数量
     */
    Long countByStatus(ArticleStatus status);

    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    @Query("select article from Article article inner join article.channels channel where channel = :channel")
    Page<Article> findByChannelsContains(@Param("channel") Channel channel, Pageable pageable);

    @Query("select article from Article article inner join article.channels channel where channel = :channel and article.status = :status")
    Page<Article> findByChannelsAndStatus(@Param("channel") Channel channel, @Param("status") ArticleStatus status, Pageable pageable);

    @Query("select distinct article from Article article join article.channels channel where channel in :channels and article.status = :status")
    Page<Article> findByChannelsContainingAndStatus(@Param("channels") Set<Channel> channels, @Param("status") ArticleStatus status, Pageable pageable);

    @Query("select distinct article from Article article join article.channels channel where  channel in :channels and article.status in :status")
    Page<Article> findByChannelsAndStatuses(@Param("channels") Set<Channel> channels, @Param("status") Set<ArticleStatus> statuses, Pageable pageable);

    Page<Article> findByUserId(String userId, Pageable pageable);

    Page<Article> findByUserIdAndStatus(String userId, ArticleStatus status, Pageable pageable);
}
