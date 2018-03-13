package com.qxcmp.redeem;

import com.google.common.collect.Lists;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.exception.RedeemKeyException;
import com.qxcmp.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    /**
     * 生成一批兑换码
     *
     * @param type        业务名称
     * @param content     业务数据
     * @param dateExpired 过期时间
     * @param quantity    生成数量
     *
     * @return 生成后的兑换码
     */
    public List<RedeemKey> generate(String type, String content, Date dateExpired, int quantity) {
        List<RedeemKey> redeemKeys = Lists.newArrayList();
        for (int i = 0; i < quantity; i++) {
            redeemKeys.add(create(() -> {
                RedeemKey next = next();
                next.setType(type);
                next.setContent(content);
                next.setDateExpired(dateExpired);
                return next;
            }));
        }
        return redeemKeys;
    }

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
