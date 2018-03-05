package com.qxcmp.mall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String>, JpaSpecificationExecutor<ShoppingCart> {

    /**
     * 根据用于ID获取购物车
     *
     * @param userId 用户ID
     * @return 用户ID对应的购物车
     */
    Optional<ShoppingCart> findByUserId(String userId);
}
