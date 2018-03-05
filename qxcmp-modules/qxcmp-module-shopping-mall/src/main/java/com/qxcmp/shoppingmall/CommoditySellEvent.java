package com.qxcmp.shoppingmall;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 商品销售事件
 * <p>
 * 该事件会在用户支付订单以后触发
 * <p>
 * 购物车里面每一个购物项会触发一个事件
 *
 * @author aaric
 */
@Data
@AllArgsConstructor
public class CommoditySellEvent {

    /**
     * 购买商品的用户ID
     */
    private String userId;

    /**
     * 销售的商品
     */
    private Commodity commodity;

    /**
     * 商品数量
     */
    private Long commodityQuantity;
}
