package com.qxcmp.account;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    /**
     * 为用户设置密保
     *
     * @param userId           用户ID
     * @param securityQuestion 密保问题
     */
    public void setForUser(String userId, AccountSecurityQuestion securityQuestion) {
        findByUserId(userId).map(question -> update(question.getId(), q -> {
            /*
             * 避免ID被自动覆盖
             * */
            String id = q.getId();
            mergeToEntity(securityQuestion, q);
            q.setId(id);
            q.setUserId(userId);
        })).orElseGet(() -> {
            securityQuestion.setUserId(userId);
            return create(securityQuestion);
        });
    }

    @Override
    public AccountSecurityQuestion create(AccountSecurityQuestion entity) {
        entity.setId(IDGenerator.next());
        return super.create(entity);
    }
}
