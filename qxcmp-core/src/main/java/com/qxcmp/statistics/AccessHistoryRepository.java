package com.qxcmp.statistics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
interface AccessHistoryRepository extends JpaRepository<AccessHistory, Long>, JpaSpecificationExecutor<AccessHistory> {

    @Query("select new com.qxcmp.statistics.AccessHistoryPageResult(a.url, count(a)) from AccessHistory a where a.dateCreated > :date group by a.url order by count(a) desc")
    Page<AccessHistoryPageResult> findByDateCreatedAfter(@Param("date") Date date, Pageable pageable);

    @Query("select new com.qxcmp.statistics.AccessHistoryPageResult(a.url, count(a)) from AccessHistory a group by a.url order by count(a) desc")
    Page<AccessHistoryPageResult> findAllResult(Pageable pageable);
}
