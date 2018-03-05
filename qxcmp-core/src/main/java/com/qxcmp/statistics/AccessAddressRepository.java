package com.qxcmp.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface AccessAddressRepository extends JpaRepository<AccessAddress, String>, JpaSpecificationExecutor<AccessAddress> {
}
