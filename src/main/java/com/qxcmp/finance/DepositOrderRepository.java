package com.qxcmp.finance;

import com.qxcmp.mall.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface DepositOrderRepository extends JpaRepository<DepositOrder, String>, JpaSpecificationExecutor<DepositOrder> {

    /**
     * 查询某一状态的订单
     *
     * @param status   状态
     * @param pageable 分页信息
     *
     * @return 某一状态的订单
     */
    Page<DepositOrder> findByStatusOrderByDateFinishedDesc(OrderStatusEnum status, Pageable pageable);
}
