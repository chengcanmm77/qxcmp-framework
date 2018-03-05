package com.qxcmp.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface WalletRepository extends JpaRepository<Wallet, String>, JpaSpecificationExecutor<Wallet> {

    /**
     * 根据用于ID获取钱包
     *
     * @param userId 用户ID
     * @return 用户ID对应的钱包
     */
    Optional<Wallet> findByUserId(String userId);
}
