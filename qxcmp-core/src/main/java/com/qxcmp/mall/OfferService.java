package com.qxcmp.mall;

import com.google.common.collect.Sets;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * 平台券服务
 * <p>
 * 支持以下服务 <ol> <li>查询用户领取券的信息</li> <li>为用户领取券</li> <li>批量发布券</li> </ol>
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class OfferService extends AbstractEntityService<Offer, String, OfferRepository> {

    private final UserService userService;
    private final ApplicationContext applicationContext;

    public Page<Offer> findByUserId(String userId, Pageable pageable) {
        return repository.findByUserIdOrderByDateReceivedDesc(userId, pageable);
    }

    /**
     * 平台发布券统一接口，商户通用一个券原型来发布
     * <p>
     * 要获取或者领取券，需要根据券的类型和名称获取
     *
     * @param prototype 券原型
     * @param quantity  要发布的券的数量
     *
     * @return 发布券以后的ID
     */
    public Set<String> publish(Consumer<Offer> prototype, int quantity) {
        checkArgument(quantity > 0, "Offer quantity must great than 0");
        Offer offer = next();
        prototype.accept(offer);
        checkState(StringUtils.isNotBlank(offer.getName()), "Offer name can't be empty");
        checkState(StringUtils.isNotBlank(offer.getType()), "Offer type can't be empty");
        checkState(StringUtils.isNotBlank(offer.getVendorID()), "Offer vendor can't be empty");
        checkState(Objects.nonNull(offer.getDateStart()), "Offer start date can't be empty");
        checkState(Objects.nonNull(offer.getDateEnd()), "Offer end date can't be empty");
        checkState(offer.getDateEnd().getTime() - offer.getDateStart().getTime() > 0, "Offer end date must great than start date");

        Set<String> offerIds = Sets.newHashSet();

        for (int i = 0; i < quantity; i++) {
            Offer o = create(() -> {
                offer.setId(null);
                return offer;
            });
            offerIds.add(o.getId());
        }

        return offerIds;
    }

    /**
     * 平台领券统一接口
     * <p>
     * 当用户存在，且券还没有被领取的时候才进行领取操作
     *
     * @param userId  用户ID
     * @param offerId 券ID
     *
     * @return 领取后的券，如果领取失败返回空
     */
    public Optional<Offer> pick(String userId, String offerId) {
        return userService.findOne(userId).map(user -> {

            Optional<Offer> offerOptional = findOne(offerId);

            if (!offerOptional.isPresent() || StringUtils.isNotBlank(offerOptional.get().getUserId())) {
                return null;
            }

            return update(offerId, offer -> {
                offer.setUserId(userId);
                offer.setStatus(OfferStatus.PICKED);
                offer.setDateReceived(new Date());
            });

        });
    }

    /**
     * 使用一个券
     * <p>
     * 若成功使用一个券，会发出券使用事件
     *
     * @param userId  使用券的用户ID
     * @param offerId 使用的券ID
     *
     * @return 券是否使用成功
     */
    public void comsume(String userId, String offerId) throws Exception {

        Optional<Offer> offerOptional = findOne(offerId);

        if (!offerOptional.isPresent()) {
            throw new Exception("券不存在");
        }

        Offer offer = offerOptional.get();

        if (!offer.getStatus().equals(OfferStatus.PICKED)) {
            throw new Exception("无效的券状态：" + offer.getStatus().getValue());
        }

        if (!StringUtils.equals(userId, offer.getUserId())) {
            throw new Exception("券的领取用户不一致");
        }

        if (System.currentTimeMillis() - offer.getDateEnd().getTime() > 0) {
            update(offer.getId(), o -> o.setStatus(OfferStatus.EXPIRED));
            throw new Exception("券已过期");
        }

        applicationContext.publishEvent(new OfferEvent(update(offer.getId(), o -> {
            o.setDateUsed(new Date());
            o.setStatus(OfferStatus.USED);
        })));
    }

    @Override
    public Offer create(Supplier<Offer> supplier) {
        Offer entity = supplier.get();

        if (StringUtils.isNotEmpty(entity.getId())) {
            return null;
        }

        entity.setId(IDGenerator.next());
        entity.setDateCreated(new Date());
        entity.setStatus(OfferStatus.NEW);

        return super.create(() -> entity);
    }
}
