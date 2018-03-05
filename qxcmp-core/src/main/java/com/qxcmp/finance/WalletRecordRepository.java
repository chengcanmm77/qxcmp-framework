package com.qxcmp.finance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface WalletRecordRepository extends JpaRepository<WalletRecord, Long>, JpaSpecificationExecutor<WalletRecord> {

    /**
     * 查找用户某一类型的钱包消费记录
     *
     * @param userId   用户主键
     * @param type     消费类型
     * @param pageable 分页信息
     * @return 查询结果
     */
    Page<WalletRecord> findByUserIdAndTypeOrderByDate(String userId, String type, Pageable pageable);
}
