package com.qxcmp.finance;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.exception.FinanceException;
import com.qxcmp.exception.OrderExpiredException;
import com.qxcmp.exception.OrderStatusException;
import com.qxcmp.mall.OrderStatusEnum;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;


/**
 * 充值订单服务
 * <p>
 * 平台账户充值服务
 * <p>
 * 平台充值流程如下 <ol> <li>生成充值订单 {@link DepositOrder}</li> <li>调用相关支付接口： 微信、支付宝</li> <li>用户完成支付操作</li>
 * <li>收到支付接口发来的回调消息，消息里面应该包含充值订单号</li> <li>调用平台充值服务接口处理充值订单</li> <li>处理订单成功以后，给用户的钱包里面增加对应金钱</li> </ol>
 *
 * @author aaric
 * @see DepositOrder
 */
@Service
public class DepositOrderService extends AbstractEntityService<DepositOrder, String, DepositOrderRepository> {

    private final ApplicationContext applicationContext;

    private final WalletService walletService;

    public DepositOrderService(DepositOrderRepository repository, ApplicationContext applicationContext, WalletService walletService) {
        super(repository);
        this.applicationContext = applicationContext;
        this.walletService = walletService;
    }

    /**
     * 查询已完成的订单
     *
     * @param pageable 分页信息
     *
     * @return 已完成的订单
     */
    public Page<DepositOrder> findFinishedOrder(Pageable pageable) {
        return repository.findByStatusOrderByDateFinishedDesc(OrderStatusEnum.FINISHED, pageable);
    }

    /**
     * 创建一个充值订单
     * <p>
     * 需要设置订单的金额，货币类型可选
     * <p>
     * 目前重置订单有效期为五分钟
     *
     * @param supplier 提单提供者
     * @param <S>      订单类型
     *
     * @return 保存后的充值订单
     */
    @Override
    public <S extends DepositOrder> Optional<S> create(Supplier<S> supplier) {
        S order = supplier.get();
        order.setId(IDGenerator.order());
        order.setTimeStart(new Date());
        order.setTimeEnd(new Date(System.currentTimeMillis() + 1000 * 60 * 5));

        order.setStatus(OrderStatusEnum.NEW);
        return super.create(() -> order);
    }

    /**
     * 处理一个订单，为用户钱包增加相应金额
     *
     * @param orderID 订单号
     *
     * @throws FinanceException 如果订单不存在，或者状态不正确，抛出该异常
     */
    public void process(String orderID) throws FinanceException {
        DepositOrder depositOrder = findOne(orderID).orElseThrow(() -> new OrderStatusException("订单不存在"));

        if (depositOrder.getFee() < 0) {
            throw new OrderStatusException("订单金额为负数");
        }

        if (!depositOrder.getStatus().equals(OrderStatusEnum.NEW)) {
            throw new OrderStatusException("无效的订单状态：" + depositOrder.getStatus().getValue());
        }

        if (depositOrder.getTimeEnd().getTime() - System.currentTimeMillis() < 0) {
            throw new OrderExpiredException("订单已过期");
        }

        try {
            walletService.changeBalance(depositOrder.getUserId(), depositOrder.getFee(), "钱包充值", "");
            update(depositOrder.getId(), order -> {
                order.setDateFinished(new Date());
                order.setStatus(OrderStatusEnum.FINISHED);
            }).ifPresent(order -> applicationContext.publishEvent(new DepositEvent(order)));
        } catch (Exception e) {
            update(depositOrder.getId(), order -> order.setStatus(OrderStatusEnum.EXCEPTION));
        }
    }

    @Override
    protected <S extends DepositOrder> String getEntityId(S entity) {
        return entity.getId();
    }
}
