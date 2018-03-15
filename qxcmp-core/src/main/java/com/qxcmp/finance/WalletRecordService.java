package com.qxcmp.finance;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Aaric
 */
@Service
public class WalletRecordService extends AbstractEntityService<WalletRecord, Long, WalletRecordRepository> {

    public Page<WalletRecord> findByUserIdAndType(String userId, String type, Pageable pageable) {
        return repository.findByUserIdAndTypeOrderByDate(userId, type, pageable);
    }

}
