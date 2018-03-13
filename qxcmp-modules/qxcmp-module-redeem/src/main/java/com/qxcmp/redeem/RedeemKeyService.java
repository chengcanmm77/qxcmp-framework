package com.qxcmp.redeem;

import com.google.common.collect.Lists;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.user.User;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
            RedeemKey next = next();
            next.setId(IDGenerator.next());
            next.setStatus(RedeemKeyStatus.NEW);
            next.setDateCreated(new Date());
            next.setType(type);
            next.setContent(content);
            next.setDateExpired(dateExpired);
            redeemKeys.add(create(next));
        }
        return redeemKeys;
    }

    /**
     * 为用户使用一个兑换码
     * <p>
     * 如果使用成功会生成 {@link RedeemKeyEvent} 事件
     * <p>
     * 需要关注该事件进行进一步业务逻辑
     *
     * @param user 使用兑换码的用户
     * @param id   使用的兑换码主键
     *
     * @throws RedeemKeyException 如果兑换码不存在或者已过期抛出该异常
     */
    public void redeem(User user, String id) throws RedeemKeyException {
        RedeemKey key = findOne(id).filter(redeemKey -> redeemKey.getStatus().equals(RedeemKeyStatus.NEW)).orElseThrow(() -> new RedeemKeyException(RedeemKeyException.Code.NOT_EXIST));
        if (new DateTime(key.getDateExpired()).compareTo(DateTime.now()) < 0) {
            update(key.getId(), redeemKey -> redeemKey.setStatus(RedeemKeyStatus.EXPIRED));
            throw new RedeemKeyException(RedeemKeyException.Code.EXPIRED);
        }
        applicationContext.publishEvent(new RedeemKeyEvent(user, update(id, redeemKey -> {
            redeemKey.setUserId(user.getId());
            redeemKey.setDateUsed(new Date());
            redeemKey.setStatus(RedeemKeyStatus.USED);
        })));
    }
}
