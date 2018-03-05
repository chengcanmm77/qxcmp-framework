package com.qxcmp.mall;

import com.qxcmp.core.entity.AbstractEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 商品实体服务
 *
 * @author aaric
 */
@Service
public class CommodityService extends AbstractEntityService<Commodity, Long, CommodityRepository> {

    public Optional<Commodity> findOne(String id) {
        try {
            Long cId = Long.parseLong(id);
            return findOne(cId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Page<Commodity> findByStore(Store store, Pageable pageable) {
        return repository.findByStore(store, pageable);
    }

    public Page<Commodity> findByCatalog(String catalog, Pageable pageable) {
        return repository.findByCatalog(catalog, pageable);
    }

    public Page<Commodity> findByCatalogs(Set<String> catalogs, Pageable pageable) {
        return repository.findByCatalogs(catalogs, pageable);
    }

    public List<Commodity> findByParentId(Long parentId) {
        return repository.findByParentId(parentId);
    }
}
