package com.qxcmp.advertisement;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 广告服务
 *
 * @author aaric
 */
@Service
public class AdvertisementService extends AbstractEntityService<Advertisement, Long, AdvertisementRepository> {

    public Optional<Advertisement> findOne(String id) {
        try {
            Long aId = Long.parseLong(id);
            return findOne(aId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Page<Advertisement> findByType(String type, Pageable pageable) {
        return repository.findByTypeOrderByAdOrderDesc(type, pageable);
    }

}
