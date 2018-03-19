package com.qxcmp.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface FeedRepository extends JpaRepository<Feed, Long>, JpaSpecificationExecutor<Feed> {

    /**
     * 查询用户指定日期之后的Feed流
     *
     * @param owner    用户ID
     * @param pageable 分页信息
     *
     * @return 指定日期之后的Feed列表
     */
    Page<Feed> findByOwnerOrderByDateCreatedDesc(String owner, Pageable pageable);

    /**
     * 查询某类型的Feed流
     *
     * @param type     类型
     * @param pageable 分页信息
     *
     * @return 某类型的Feed流
     */
    Page<Feed> findByTypeOrderByDateCreatedDesc(String type, Pageable pageable);

    /**
     * 查询某类型的动态
     *
     * @param type 类型
     *
     * @return 某类型的动态
     */
    List<Feed> findByTypeOrderByDateCreatedDesc(String type);
}
