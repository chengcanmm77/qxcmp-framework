package com.qxcmp.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 根据名称查找角色
     *
     * @param name 角色名称
     *
     * @return 查找到的角色
     */
    Optional<Role> findByName(String name);
}
