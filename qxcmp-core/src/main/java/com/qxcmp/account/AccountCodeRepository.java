package com.qxcmp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface AccountCodeRepository extends JpaRepository<AccountCode, String>, JpaSpecificationExecutor<AccountCode> {
}
