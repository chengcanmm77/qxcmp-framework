package com.qxcmp.weixin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface WeixinMpMaterialRepository extends JpaRepository<WeixinMpMaterial, String>, JpaSpecificationExecutor<WeixinMpMaterial> {
}
