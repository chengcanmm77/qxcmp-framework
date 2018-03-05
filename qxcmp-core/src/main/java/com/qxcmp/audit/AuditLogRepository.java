package com.qxcmp.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    /**
     * 审计日志分页查询接口，按照发生事件降序排列
     *
     * @param pageable 分页查询参数
     *
     * @return 分页查询结果
     */
    Page<AuditLog> findAllByOrderByDateCreatedDesc(Pageable pageable);
}
