package com.qxcmp.mall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long>, JpaSpecificationExecutor<ShoppingCartItem> {

    /**
     * 获取用户的购物车项目
     *
     * @param userId 用户ID
     *
     * @return 用户的购物车项目
     */
    List<ShoppingCartItem> findByUserIdOrderByDateCreatedDesc(String userId);
}
