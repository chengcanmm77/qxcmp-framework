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
interface SearchKeywordsRepository extends JpaRepository<SearchKeywords, Long>, JpaSpecificationExecutor<SearchKeywords> {

    @Query("select new com.qxcmp.statistics.SearchKeywordsPageResult(a.title, count(a)) from SearchKeywords a where a.dateCreated > :date group by a.title order by count(a) desc")
    Page<SearchKeywordsPageResult> findByDateCreatedAfter(@Param("date") Date date, Pageable pageable);

    @Query("select new com.qxcmp.statistics.SearchKeywordsPageResult(a.title, count(a)) from SearchKeywords a group by a.title order by count(a) desc")
    Page<SearchKeywordsPageResult> findAllResult(Pageable pageable);
}
