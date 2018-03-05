package com.qxcmp.shoppingmall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ConsigneeRepository extends JpaRepository<Consignee, String>, JpaSpecificationExecutor<Consignee> {

    /**
     * 获取用户收货地址
     *
     * @param userId 用户ID
     *
     * @return 用户收货地址
     */
    List<Consignee> findByUserIdOrderByDateModifiedDesc(String userId);
}
