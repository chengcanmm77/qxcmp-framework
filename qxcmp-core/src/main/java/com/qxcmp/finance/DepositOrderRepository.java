package com.qxcmp.finance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface DepositOrderRepository extends JpaRepository<DepositOrder, String>, JpaSpecificationExecutor<DepositOrder> {

    /**
     * 查询某一状态的订单
     *
     * @param status 状态
     *
     * @return 某一状态的订单
     */
    List<DepositOrder> findByStatusOrderByDateFinishedDesc(OrderStatusEnum status);

    /**
     * 查询某一状态的订单
     *
     * @param status   状态
     * @param pageable 分页信息
     *
     * @return 某一状态的订单
     */
    Page<DepositOrder> findByStatusOrderByDateFinishedDesc(OrderStatusEnum status, Pageable pageable);

    /**
     * 查询某用户的订单
     *
     * @param userId   用户ID
     * @param status   状态
     * @param pageable 分页信息
     *
     * @return 用户某状态的订单
     */
    Page<DepositOrder> findByUserIdAndStatusOrderByDateFinishedDesc(String userId, OrderStatusEnum status, Pageable pageable);
}
