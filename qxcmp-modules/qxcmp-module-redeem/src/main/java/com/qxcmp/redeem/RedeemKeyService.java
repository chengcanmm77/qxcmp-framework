package com.qxcmp.redeem;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.exception.RedeemKeyException;
import com.qxcmp.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Supplier;

/**
 * 兑换码服务
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class RedeemKeyService extends AbstractEntityService<RedeemKey, String, RedeemKeyRepository> {

    private final ApplicationContext applicationContext;

    public void redeem(User user, String id) throws RedeemKeyException {
        RedeemKey key = findOne(id).filter(redeemKey -> !redeemKey.getStatus().equals(RedeemKeyStatus.USED)).orElseThrow(() -> new RedeemKeyException("Invalid id"));

        if (System.currentTimeMillis() - key.getDateExpired().getTime() > 0) {
            try {
                update(id, redeemKey -> redeemKey.setStatus(RedeemKeyStatus.EXPIRED));
                throw new RedeemKeyException("Expired");
            } catch (Exception e) {
                throw new RedeemKeyException(e.getMessage(), e);
            }
        }

        try {
            applicationContext.publishEvent(new RedeemKeyEvent(user, update(id, redeemKey -> {
                redeemKey.setUserId(user.getId());
                redeemKey.setDateUsed(new Date());
                redeemKey.setStatus(RedeemKeyStatus.USED);
            })));
        } catch (Exception e) {
            throw new RedeemKeyException(e.getMessage(), e);
        }
    }

    @Override
    public RedeemKey create(Supplier<RedeemKey> supplier) {
        RedeemKey entity = supplier.get();
        entity.setId(IDGenerator.next());
        entity.setStatus(RedeemKeyStatus.NEW);
        entity.setDateCreated(new Date());
        return super.create(entity);
    }
}
