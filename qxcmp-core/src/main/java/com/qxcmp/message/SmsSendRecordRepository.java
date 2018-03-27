package com.qxcmp.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
interface SmsSendRecordRepository extends JpaRepository<SmsSendRecord, String>, JpaSpecificationExecutor<SmsSendRecord> {

    /**
     * 计算某日期后某状态的短信发送数量
     *
     * @param dateCreated 日期
     * @param code        状态
     * @return 某日期后某状态的短信发送数量
     */
    Long countByDateCreatedAfterAndCode(Date dateCreated, String code);
}
