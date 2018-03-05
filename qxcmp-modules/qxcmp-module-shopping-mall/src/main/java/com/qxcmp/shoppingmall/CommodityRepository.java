package com.qxcmp.shoppingmall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
interface CommodityRepository extends JpaRepository<Commodity, Long>, JpaSpecificationExecutor<Commodity> {

    /**
     * 查询店铺拥有的商品
     */
    Page<Commodity> findByStore(Store store, Pageable pageable);

    /**
     * 根据商品分类查找
     * <p>
     * 会过滤掉下架商品
     */
    @Query("select commodity from Commodity commodity left join commodity.catalogs catalog where commodity.disabled = false and catalog = :catalog order by commodity.dateModified")
    Page<Commodity> findByCatalog(@Param("catalog") String catalog, Pageable pageable);

    /**
     * 根据商品分类查找
     * <p>
     * 会过滤掉下架商品
     */
    @Query("select commodity from Commodity commodity left join commodity.catalogs catalog where commodity.disabled = false and catalog in :catalogs order by commodity.dateModified")
    Page<Commodity> findByCatalogs(@Param("catalogs") Set<String> catalogs, Pageable pageable);

    /**
     * 查找关联商品
     *
     * @param parentId 关联商品ID
     * @return 关联商品
     */
    @Query("select commodity from Commodity commodity where commodity.disabled = false and commodity.parentId = :parentId")
    List<Commodity> findByParentId(@Param("parentId") Long parentId);
}
