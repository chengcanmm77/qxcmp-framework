package com.qxcmp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AccountSecurityQuestionRepository extends JpaRepository<AccountSecurityQuestion, String>, JpaSpecificationExecutor<AccountSecurityQuestion> {

    Optional<AccountSecurityQuestion> findByUserId(String userId);
}
