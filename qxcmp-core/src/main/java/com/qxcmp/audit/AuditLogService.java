package com.qxcmp.audit;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 审计日志服务
 *
 * @author aaric
 * @see AbstractEntityService
 */
@Service
public class AuditLogService extends AbstractEntityService<AuditLog, Long, AuditLogRepository> {

    public Optional<AuditLog> findOne(String id) {
        try {
            return findOne(Long.parseLong(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<AuditLog> findAll(Pageable pageable) {
        return repository.findAllByOrderByDateCreatedDesc(pageable);
    }

}
