package com.qxcmp.mall;

import com.qxcmp.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface StoreRepository extends JpaRepository<Store, String>, JpaSpecificationExecutor<Store> {

    /**
     * 查找用户拥有的店铺
     * <p>
     * 当用户为店铺所有者或者管理员的时候会被入选
     *
     * @param user     用户
     * @param pageable 分页信息
     * @return 用户拥有的店铺
     */
    @Query("select store from Store store where store.owner = :user")
    Page<Store> findByUser(@Param("user") User user, Pageable pageable);

    @Query("select store from Store store where store.owner = :user")
    List<Store> findByUser(@Param("user") User user);

}
