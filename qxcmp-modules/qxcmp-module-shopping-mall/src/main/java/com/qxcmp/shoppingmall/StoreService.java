package com.qxcmp.shoppingmall;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class StoreService extends AbstractEntityService<Store, String, StoreRepository> {


    public Page<Store> findByUser(User user, Pageable pageable) {
        return repository.findByUser(user, pageable);
    }

    public List<Store> findByUser(User user) {
        return repository.findByUser(user);
    }


    @Override
    public Store create(Supplier<Store> supplier) {
        Store store = supplier.get();

        if (StringUtils.isNotEmpty(store.getId())) {
            return null;
        }

        store.setId(IDGenerator.next());

        return super.create(() -> store);
    }
}
