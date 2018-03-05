package com.qxcmp.statistics;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Aaric
 */
@Service
public class AccessHistoryService extends AbstractEntityService<AccessHistory, Long, AccessHistoryRepository> {

    public Page<AccessHistoryPageResult> findByDateCreatedAfter(Date date, Pageable pageable) {
        return repository.findByDateCreatedAfter(date, pageable);
    }

    public Page<AccessHistoryPageResult> findAllResult(Pageable pageable) {
        return repository.findAllResult(pageable);
    }
}
