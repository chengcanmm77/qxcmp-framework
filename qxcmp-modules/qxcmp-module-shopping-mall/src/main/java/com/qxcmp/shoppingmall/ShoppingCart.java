package com.qxcmp.shoppingmall;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 用户购物车
 * <p>
 * 存放了购物车结算时候的配置
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class ShoppingCart {

    /**
     * 购物车主键，由平台生成，使用{@link UUID#randomUUID()}生成
     */
    @Id
    private String id;

    /**
     * 购物车所属用户主键
     */
    @Column(unique = true)
    @NotNull
    private String userId;

    /**
     * 结算时的收货人ID
     */
    private String consigneeId;

    /**
     * 结算时是否使用积分
     */
    private boolean usePoints;

    /**
     * 结算时使用的积分数量
     */
    private long points;

}
