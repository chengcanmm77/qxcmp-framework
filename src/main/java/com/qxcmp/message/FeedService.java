package com.qxcmp.message;

import com.qxcmp.core.entity.AbstractEntityService;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Aaric
 */
@Service
public class FeedService extends AbstractEntityService<Feed, Long, FeedRepository> {

    /**
     * 查询用户的Feed流
     *
     * @param userId 用户ID
     *
     * @return 用户Feed流列表
     */
    public Page<Feed> findByOwner(String userId, Pageable pageable) {
        return repository.findByOwnerAndDateCreatedAfterOrderByDateCreatedDesc(userId, DateTime.now().minusDays(30).toDate(), pageable);
    }

    public Page<Feed> findByType(String type, Pageable pageable) {
        return repository.findByTypeOrderByDateCreatedDesc(type, pageable);
    }

}
