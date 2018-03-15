package com.qxcmp.shoppingmall;

import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.exception.FinanceException;
import com.qxcmp.exception.NoBalanceException;
import com.qxcmp.exception.OrderStatusException;
import com.qxcmp.finance.OrderStatusEnum;
import com.qxcmp.finance.Wallet;
import com.qxcmp.finance.WalletService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 商品订单服务
 * <p>
 * 支持以下服务： <ol> <li>商品订单创建</li> <li>商品订单处理</li> <li>商品订单查询</li> </ol>
 *
 * @author aaric
 */
@Service
@RequiredArgsConstructor
public class CommodityOrderService extends AbstractEntityService<CommodityOrder, String, CommodityOrderRepository> {

    private final ApplicationContext applicationContext;
    private final WalletService walletService;
    private final ShoppingCartItemService shoppingCartItemService;
    private final CommodityOrderItemService commodityOrderItemService;
    private final ConsigneeService consigneeService;


    /**
     * 商品统一下单接口
     *
     * @param userId       用户ID
     * @param items        用户选择的购物车项目
     * @param shoppingCart 用户购物车
     *
     * @return 创建好的商品订单
     */
    public Optional<CommodityOrder> order(String userId, List<ShoppingCartItem> items, ShoppingCart shoppingCart) {

        if (items.isEmpty()) {
            return Optional.empty();
        }

        CommodityOrder order = create(() -> {
            CommodityOrder commodityOrder = next();
            commodityOrder.setUserId(userId);
            commodityOrder.setActualPayment(getTotalItemPrice(items));
            commodityOrder.setActualPoint(getTotalItemPoint(items));
            consigneeService.findOne(shoppingCart.getConsigneeId()).ifPresent(consignee -> {
                commodityOrder.setConsigneeName(consignee.getConsigneeName());
                commodityOrder.setConsigneePhone(consignee.getTelephone());
                commodityOrder.setConsigneeEmail(consignee.getEmail());
                commodityOrder.setAddress(consignee.getAddress());
            });
            return commodityOrder;
        });

        items.forEach(shoppingCartItem -> commodityOrderItemService.create(() -> {
            CommodityOrderItem commodityOrderItem = commodityOrderItemService.next();
            commodityOrderItem.setQuantity(shoppingCartItem.getQuantity());
            commodityOrderItem.setCommodity(shoppingCartItem.getCommodity());
            commodityOrderItem.setActualPrice(shoppingCartItem.getCommodity().getSellPrice());
            commodityOrderItem.setCommodityOrder(order);
            return commodityOrderItem;
        }));

        items.forEach(shoppingCartItem -> shoppingCartItemService.deleteById(shoppingCartItem.getId()));

        return Optional.ofNullable(order);
    }

    /**
     * 商品订单统一支付接口
     * <p>
     * 调用该接口对商品订单进行支付，并把订单状态标记为已付款
     * <p>
     * 订单支付成功以后会发送订单完成事件
     *
     * @param orderId 订单号
     *
     * @return 支付后的订单
     *
     * @throws FinanceException 如果用户余额不足，或者其他异常，抛出该异常
     */
    public Optional<CommodityOrder> pay(String orderId) throws FinanceException {
        Optional<CommodityOrder> commodityOrderOptional = findOne(orderId);

        if (!commodityOrderOptional.isPresent()) {
            throw new OrderStatusException("Order not exist");
        }

        CommodityOrder commodityOrder = commodityOrderOptional.get();

        if (!commodityOrder.getStatus().equals(OrderStatusEnum.PAYING)) {
            throw new OrderStatusException("Order status is not paying");
        }

        Optional<Wallet> walletOptional = walletService.findByUserId(commodityOrder.getUserId());

        if (!walletOptional.isPresent()) {
            throw new OrderStatusException("User not exist");
        }

        Wallet wallet = walletOptional.get();

        int orderPrice = commodityOrder.getActualPayment();
        int orderPoint = commodityOrder.getActualPoint();

        if (wallet.getBalance() < orderPrice) {
            throw new NoBalanceException("No balance");
        }

        if (wallet.getPoints() < orderPoint) {
            throw new NoBalanceException("No point");
        }

        walletService.update(wallet.getId(), w -> {
            w.setBalance(w.getBalance() - orderPrice);
            w.setPoints(w.getPoints() - orderPoint);
        });

        CommodityOrder updatedCommodity = update(commodityOrder.getId(), order -> {
            order.setStatus(OrderStatusEnum.PAYED);
            order.setActualPayment(orderPrice);
            order.setActualPoint(orderPoint);
        });


        /*
         * 发送商品销售事件
         * */
        updatedCommodity.getItems().forEach(commodityOrderItem -> applicationContext.publishEvent(new CommoditySellEvent(commodityOrder.getUserId(), commodityOrderItem.getCommodity(), (long) commodityOrderItem.getQuantity())));

        return Optional.of(updatedCommodity);
    }

    /**
     * 查询用户所有订单
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     *
     * @return 查询结果
     */
    public Page<CommodityOrder> findByUserId(String userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }


    /**
     * 根据订单状态查询订单记录
     *
     * @param userId   用户ID
     * @param status   订单状态
     * @param pageable 分页信息
     *
     * @return 查询结果
     */
    public Page<CommodityOrder> findByUserIdAndStatus(String userId, OrderStatusEnum status, Pageable pageable) {
        return repository.findByUserIdAndStatus(userId, status, pageable);
    }

    @Override
    public CommodityOrder create(Supplier<CommodityOrder> supplier) {
        CommodityOrder entity = supplier.get();

        if (StringUtils.isNotEmpty(entity.getId())) {
            return null;
        }

        entity.setId(IDGenerator.order());
        entity.setDateCreated(new Date());
        entity.setStatus(OrderStatusEnum.PAYING);

        return super.create(() -> entity);
    }

    private int getTotalItemPrice(List<ShoppingCartItem> items) {
        return items.stream().map(shoppingCartItem -> shoppingCartItem.getCommodity().getSellPrice() * shoppingCartItem.getQuantity()).reduce(0, (sum, price) -> sum + price);
    }

    private int getTotalItemPoint(List<ShoppingCartItem> items) {
        return items.stream().map(shoppingCartItem -> shoppingCartItem.getCommodity().getPoint() * shoppingCartItem.getQuantity()).reduce(0, (sum, price) -> sum + price);
    }
}
