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

    /**
     * 查询某状态的文章
     *
     * @param status   状态
     * @param pageable 分页信息
     * @return 某状态的文章
     */
    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    /**
     * 查询一个栏目中指定状态的文章
     *
     * @param channel  栏目
     * @param status   状态
     * @param pageable 分页信息
     * @return 一个栏目中指定状态的文章
     */
    @Query("select article from Article article inner join article.channels channel where channel = :channel and article.status = :status")
    Page<Article> findByChannelsAndStatus(@Param("channel") Channel channel, @Param("status") ArticleStatus status, Pageable pageable);

    /**
     * 查询指定栏目中指定状态的文章
     *
     * @param channels 栏目
     * @param statuses 状态
     * @param pageable 分页信息
     * @return 指定栏目中指定状态的文章
     */
    @Query("select distinct article from Article article join article.channels channel where channel in :channels and article.status in :status")
    Page<Article> findByChannelsAndStatuses(@Param("channels") Set<Channel> channels, @Param("status") Set<ArticleStatus> statuses, Pageable pageable);

    /**
     * 查询用户拥有的文章
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     * @return 用户拥有的文章
     */
    Page<Article> findByUserIdOrderByDateModifiedDesc(String userId, Pageable pageable);

    /**
     * 查询用户某状态的文章
     *
     * @param userId   用户ID
     * @param status   状态
     * @param pageable 分页信息
     * @return 用户某状态的文章
     */
    Page<Article> findByUserIdAndStatusOrderByDateModifiedDesc(String userId, ArticleStatus status, Pageable pageable);

    /**
     * 计算用户某状态文章数量
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 用户某状态文章数量
     */
    Long countByUserIdAndStatus(String userId, ArticleStatus status);
}
