package com.qxcmp.account;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 密保问题服务
 *
 * @author aaric
 */
@Service
public class AccountSecurityQuestionService extends AbstractEntityService<AccountSecurityQuestion, String, AccountSecurityQuestionRepository> {

    public Optional<AccountSecurityQuestion> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public AccountSecurityQuestion create(Supplier<AccountSecurityQuestion> supplier) {
        AccountSecurityQuestion securityQuestion = supplier.get();

        if (StringUtils.isNotEmpty(securityQuestion.getId())) {
            return null;
        }

        securityQuestion.setId(IDGenerator.next());

        return super.create(() -> securityQuestion);
    }


}
