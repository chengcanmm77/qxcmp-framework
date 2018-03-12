package com.qxcmp.redeem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface RedeemKeyRepository extends JpaRepository<RedeemKey, String>, JpaSpecificationExecutor<RedeemKey> {
}
