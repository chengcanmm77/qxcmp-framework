package com.qxcmp.shoppingmall;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Supplier;

/**
 * 转账记录实体服务
 *
 * @author aaric
 */
@Service
public class TransferRecordService extends AbstractEntityService<TransferRecord, String, TransferRecordRepository> {

    @Override
    public TransferRecord create(Supplier<TransferRecord> supplier) {
        TransferRecord entity = supplier.get();

        entity.setId(IDGenerator.order());
        entity.setDateCreated(new Date());

        return super.create(() -> entity);
    }
}
