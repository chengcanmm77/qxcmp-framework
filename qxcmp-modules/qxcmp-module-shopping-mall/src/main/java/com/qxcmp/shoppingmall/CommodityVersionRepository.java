package com.qxcmp.shoppingmall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface CommodityVersionRepository extends JpaRepository<CommodityVersion, Long>, JpaSpecificationExecutor<CommodityVersion> {
}
