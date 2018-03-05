package com.qxcmp.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserConfigRepository extends JpaRepository<UserConfig, Long>, JpaSpecificationExecutor<UserConfig> {

    Optional<UserConfig> findByUserIdAndName(String userId, String name);
}
