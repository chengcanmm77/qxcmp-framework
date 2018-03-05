package com.qxcmp.mall;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

/**
 * 收货人服务
 *
 * @author aaric
 */
@Service
public class ConsigneeService extends AbstractEntityService<Consignee, String, ConsigneeRepository> {

    /**
     * 获取用户收货地址
     *
     * @param userId 用户ID
     *
     * @return 用户收货地址
     */
    public List<Consignee> findByUser(String userId) {
        return repository.findByUserIdOrderByDateModifiedDesc(userId);
    }

    @Override
    public Consignee create(Supplier<Consignee> supplier) {
        Consignee consignee = supplier.get();

        if (StringUtils.isNotEmpty(consignee.getId())) {
            return null;
        }

        consignee.setId(IDGenerator.next());

        return super.create(() -> consignee);
    }
}
