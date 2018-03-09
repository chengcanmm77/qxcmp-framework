package com.qxcmp.link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface LinkRepository extends JpaRepository<Link, Long>, JpaSpecificationExecutor<Link> {

    /**
     * 查询指定类型的链接
     *
     * @param type 链接类型
     * @return 所有指定类型的链接
     */
    List<Link> findByTypeOrderBySort(String type);

}
