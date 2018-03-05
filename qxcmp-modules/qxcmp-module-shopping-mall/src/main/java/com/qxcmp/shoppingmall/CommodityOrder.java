package com.qxcmp.shoppingmall;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 商品订单
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class CommodityOrder {

    /**
     * 订单号，由平台自动生成
     */
    @Id
    private String id;

    /**
     * 订单所属用户ID
     */
    @NotNull
    private String userId;

    /**
     * 下单时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateCreated;

    /**
     * 订单状态
     */
    private OrderStatusEnum status;

    /**
     * 订单总金额
     */
    private int actualPayment;

    /**
     * 订单总积分
     */
    private int actualPoint;

    /**
     * 订单项目
     */
    @OneToMany(mappedBy = "commodityOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CommodityOrderItem> items = Lists.newArrayList();

    /**
     * 收货人姓名
     */
    private String consigneeName;

    /**
     * 送货地址
     */
    private String address;

    /**
     * 收货人手机
     */
    private String consigneePhone;

    /**
     * 收货人邮箱
     */
    private String consigneeEmail;
}
