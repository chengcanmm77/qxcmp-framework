package com.qxcmp.shoppingmall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface CommodityOrderRepository extends JpaRepository<CommodityOrder, String>, JpaSpecificationExecutor<CommodityOrder> {

    /**
     * 查询用户所有订单
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     * @return 查询结果
     */
    Page<CommodityOrder> findByUserId(String userId, Pageable pageable);

    /**
     * 根据订单状态查询订单记录
     *
     * @param userId   用户ID
     * @param status   订单状态
     * @param pageable 分页信息
     * @return 查询结果
     */
    Page<CommodityOrder> findByUserIdAndStatus(String userId, OrderStatusEnum status, Pageable pageable);
}
