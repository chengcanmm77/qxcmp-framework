package com.qxcmp.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RegionRepository extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region> {

    /**
     * 获取某一级别的地区
     *
     * @param level 级别
     * @return 某一级别的地区
     */
    @Query("select region from Region region where region.disable = false and region.level = :level")
    List<Region> findByLevel(@Param("level") RegionLevel level);

    /**
     * 获取地区的所有直接下级地区实体
     *
     * @param parent 父级地区
     * @return 下级地区
     */
    @Query("select region from Region region where region.disable = false and region.parent = :parent")
    List<Region> findInferiors(@Param("parent") String parent);

    @Query("select region from Region region where region.parent = :parent")
    List<Region> findAllInferiors(@Param("parent") String parent);
}
