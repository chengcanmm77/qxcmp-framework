package com.qxcmp.shoppingmall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface OfferRepository extends JpaRepository<Offer, String>, JpaSpecificationExecutor<Offer> {

    /**
     * 查询用户领取的券信息
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     * @return 用户领取券的信息
     */
    Page<Offer> findByUserIdOrderByDateReceivedDesc(String userId, Pageable pageable);
}
