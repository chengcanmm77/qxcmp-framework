package com.qxcmp.spider;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface SpiderLogRepository extends JpaRepository<SpiderLog, Long>, JpaSpecificationExecutor<SpiderLog> {

    /**
     * 日志分页查询
     *
     * @param pageable 分页参数
     * @return 查询后的结果
     */
    Page<SpiderLog> findAllByOrderByDateFinishDesc(Pageable pageable);
}
