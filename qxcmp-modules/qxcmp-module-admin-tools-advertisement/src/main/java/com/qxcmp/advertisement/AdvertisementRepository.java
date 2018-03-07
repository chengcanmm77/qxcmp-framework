package com.qxcmp.advertisement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, JpaSpecificationExecutor<Advertisement> {

    /**
     * 根据广告类型查找广告
     *
     * @param type     广告类型
     * @param pageable 分页信息
     * @return 分页查询结果
     */
    Page<Advertisement> findByTypeOrderByAdOrderDesc(String type, Pageable pageable);
}
