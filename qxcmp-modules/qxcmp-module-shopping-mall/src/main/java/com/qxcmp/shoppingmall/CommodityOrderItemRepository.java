package com.qxcmp.shoppingmall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface CommodityOrderItemRepository extends JpaRepository<CommodityOrderItem, Long>, JpaSpecificationExecutor<CommodityOrderItem> {
}
