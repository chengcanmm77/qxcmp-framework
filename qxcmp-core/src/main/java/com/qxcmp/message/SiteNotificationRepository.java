package com.qxcmp.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SiteNotificationRepository extends JpaRepository<SiteNotification, Long>, JpaSpecificationExecutor<SiteNotification> {

    /**
     * 查询当前启用的网站通知
     *
     * @return 当前启用的网站通知列表
     */
    @Query("select notification from SiteNotification notification where notification.dateStart < current_time and notification.dateEnd > current_time")
    List<SiteNotification> findActive();
}
