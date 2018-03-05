package com.qxcmp.message;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InnerMessageService extends AbstractEntityService<InnerMessage, Long, InnerMessageRepository> {

    public Optional<InnerMessage> findOne(String id) {
        try {
            Long aId = Long.parseLong(id);
            return findOne(aId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Page<InnerMessage> findByUserID(String userId, Pageable pageable) {
        return repository.findByUserIdOrderByUnreadDescSendTimeDesc(userId, pageable);
    }

    public Page<InnerMessage> findUserUnreadMessages(String userId, Pageable pageable) {
        return repository.findByUserIdAndUnreadOrderBySendTimeDesc(userId, true, pageable);
    }

    public Page<InnerMessage> findUserReadMessages(String userId, Pageable pageable) {
        return repository.findByUserIdAndUnreadOrderBySendTimeDesc(userId, false, pageable);
    }

    public long countByUserId(String userId) {
        return repository.countByUserIdAndUnread(userId, true);
    }

}
