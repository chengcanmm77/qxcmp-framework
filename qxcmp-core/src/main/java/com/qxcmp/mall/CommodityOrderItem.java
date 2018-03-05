package com.qxcmp.mall;

import lombok.Data;

import javax.persistence.*;

/**
 * 商品订单项目
 * <p>
 * 用于保存商品订单所关联的商品信息
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class CommodityOrderItem {

    /**
     * 主键，有框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CommodityOrder commodityOrder;

    /**
     * 订单项目对应的商品
     */
    @ManyToOne
    private Commodity commodity;

    /**
     * 订单结算时的商品单价
     */
    private int actualPrice;

    /**
     * 订单结算时的商品数量
     */
    private int quantity;
}
