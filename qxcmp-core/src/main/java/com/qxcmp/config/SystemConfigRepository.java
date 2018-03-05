package com.qxcmp.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface SystemConfigRepository extends JpaRepository<SystemConfig, String>, JpaSpecificationExecutor<SystemConfig> {
}
