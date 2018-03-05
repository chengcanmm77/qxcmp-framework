package com.qxcmp.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface InnerMessageRepository extends JpaRepository<InnerMessage, Long>, JpaSpecificationExecutor<InnerMessage> {

    /**
     * 查询用户相关的站内信消息
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     *
     * @return 用户的所有站内信消息
     */
    Page<InnerMessage> findByUserIdOrderByUnreadDescSendTimeDesc(String userId, Pageable pageable);

    /**
     * 查询用户某一状态的站内信消息
     *
     * @param userId   用户ID
     * @param unread   是否为未读
     * @param pageable 分页信息
     *
     * @return 用户某一状态的站内信消息
     */
    Page<InnerMessage> findByUserIdAndUnreadOrderBySendTimeDesc(String userId, boolean unread, Pageable pageable);

    /**
     * 查询某一用户某状态下的站内信数量
     *
     * @param userId 用户ID
     * @param unread 是否为未读
     *
     * @return 某状态下站内信的数量
     */
    long countByUserIdAndUnread(String userId, boolean unread);
}
